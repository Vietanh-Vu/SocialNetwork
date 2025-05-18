import React, {useState, useCallback} from 'react';
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {formatDistanceToNow} from 'date-fns';
import {getAvatarFallback} from "@/lib/utils";
import {ArrowDown, ArrowUp, Heart, Reply} from "lucide-react";
import Image from "next/image";
import {Button} from "@/components/ui/button";
import Link from "next/link";
import TextExpander from "@/app/(overview)/components/ultils/TextExpander";
import {getChildComment} from "@/lib/data";
import {toast} from "sonner";
import {reactComment} from "@/lib/action";
import {useAuth} from "@/app/(overview)/components/context/AuthContext";
import Spinner from "@/app/(overview)/components/ultils/Spinner";
import MoreActionComment from "@/app/(overview)/components/comment/MoreActionComment";
import {useRouter} from "next/navigation";
import {Dialog, DialogContent, DialogTrigger} from "@/components/ui/dialog";
import CommentForm from "@/app/(overview)/components/comment/CommentForm";
import {Card} from "@/components/ui/card";
import ImageViewer from "@/app/(overview)/components/ImageViewer";

function CommentItem({comment}) {
    const {
        commentId,
        userId,
        username,
        avatar,
        postId,
        parentCommentId,
        numberOfChild,
        content,
        createdAt,
        updatedAt,
        image,
        reactCount,
        isReacted
    } = comment;
    const [childComments, setChildComments] = useState([]);
    const [showChildComments, setShowChildComments] = useState(false);
    const [isReact, setIsReact] = useState(isReacted);
    const [numOfReacts, setNumOfReacts] = useState(reactCount);
    const {currentUserId, loading} = useAuth();
    const router = useRouter();
    const [childCommentMeta, setChildCommentMeta] = useState({page: 1, hasNext: false});
    const [isLoadingMore, setIsLoadingMore] = useState(false);

    const fetchChildComments = useCallback(async (page = 1) => {
        setIsLoadingMore(true);
        const result = await getChildComment(commentId, postId, page);
        if (result.isSuccessful) {
            if (page === 1) {
                setChildComments(result.data.data);
            } else {
                setChildComments(prevComments => [...prevComments, ...result.data.data]);
            }
            setChildCommentMeta({
                page: result.data.pageMeta.page,
                hasNext: result.data.pageMeta.hasNext
            });
            setShowChildComments(true);
        } else {
            toast.error("Error while fetching child comments");
        }
        setIsLoadingMore(false);
    }, [commentId, postId]);

    async function handleClickButton() {
        if (!showChildComments) {
            await fetchChildComments();
        } else {
            setShowChildComments(false);
        }
    }

    async function handleLoadMoreChildComments() {
        if (childCommentMeta.hasNext) {
            await fetchChildComments(childCommentMeta.page + 1);
        }
    }

    async function handleReact() {
        if (isReact === true) {
            setNumOfReacts(numOfReacts - 1);
        } else {
            setNumOfReacts(numOfReacts + 1);
        }
        setIsReact(!isReact);
        const result = await reactComment(commentId)

        if (!result.isSuccessful) {
            console.log(result.message)
            toast.error("Error while reacting comment");
        }
    }

    if (loading) {
        return <Spinner/>;
    }

    return (
        <Card className="bg-card p-4 mb-3">
            <div className="flex justify-between items-start">
                <div className="flex items-center gap-3">
                    <Avatar className="w-10 h-10 border">
                        <AvatarImage src={avatar}/>
                        <AvatarFallback>{getAvatarFallback(username)}</AvatarFallback>
                    </Avatar>
                    <div className="grid gap-1">
                        <Link href={`/home/${userId}`}>
                            <div className="font-semibold">{username}</div>
                        </Link>
                        <time
                            className="text-xs text-muted-foreground">{createdAt !== updatedAt ? "Updated" : ""} {`${formatDistanceToNow(updatedAt)} ago`}</time>
                    </div>
                </div>
                <div className="flex items-center gap-2 text-muted-foreground">
                    {Number(userId) === Number(currentUserId) && (
                        <MoreActionComment commentInfo={comment}/>
                    )}
                    <Button
                        variant="ghost"
                        size="sm"
                        className="p-0"
                        onClick={handleReact}
                    >
                        <Heart
                            className={`w-5 h-5 ${isReact ? 'fill-current text-black dark:text-white' : 'dark:text-gray-400'}`}/>
                    </Button>
                    <span>{numOfReacts}</span>
                </div>
            </div>
            <div className="mt-4 text-sm">
                <p>
                    <TextExpander>{content}</TextExpander>
                </p>
            </div>
            {image && (
                <ImageViewer
                    src={image}
                    alt="Comment image"
                    width={800}
                    height={400}
                    className="mt-4 w-full rounded-lg"
                    style={{aspectRatio: "800/400", objectFit: "cover"}}
                />
            )}
            <div className="flex justify-between">
                <div>
                    {numberOfChild !== 0 && (
                        <Button variant="ghost" className="flex items-center gap-1" onClick={handleClickButton}>
                            {showChildComments === true ? <ArrowUp className="w-5 h-5"/> :
                                <ArrowDown className="w-5 h-5"/>}
                            <span className="leading-relaxed text-muted-foreground">{numberOfChild} reply</span>
                        </Button>
                    )}
                </div>
                {parentCommentId === null && (
                    <Dialog>
                        <DialogTrigger asChild>
                            <Button variant="ghost" className="flex items-center gap-1">
                                <Reply className="w-5 h-5"/>
                            </Button>
                        </DialogTrigger>
                        <DialogContent>
                            <CommentForm postId={postId} parentCommentId={commentId}/>
                        </DialogContent>
                    </Dialog>
                )}
            </div>
            {showChildComments && (
                <div className="mt-4">
                    {childComments.map(childComment => (
                        <CommentItem key={childComment.commentId} comment={childComment}/>
                    ))}
                    {childCommentMeta.hasNext && (
                        <div className="text-center mt-4">
                            <button
                                onClick={handleLoadMoreChildComments}
                                className="text-primary font-semibold hover:underline"
                                disabled={isLoadingMore}
                            >
                                {isLoadingMore ? 'Loading...' : 'Load more replies'}
                            </button>
                        </div>
                    )}
                    {isLoadingMore && (
                        <div className="mt-4 text-center">
                            <Spinner/>
                        </div>
                    )}
                </div>
            )}
        </Card>
    );
}

export default CommentItem;