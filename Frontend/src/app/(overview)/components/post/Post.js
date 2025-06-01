"use client";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import Image from "next/image";
import {Button} from "@/components/ui/button";
import {FilePen, Heart, MessageCircle, Tag} from "lucide-react";
import {capitalizeFirstLetter, getAvatarFallback} from "@/lib/utils";
import {Card, CardContent, CardHeader} from "@/components/ui/card";
import {formatDistanceToNow} from "date-fns";
import {Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious} from "@/components/ui/carousel";
import Link from "next/link";
import {useAuth} from "@/app/(overview)/components/context/AuthContext";
import Spinner from "@/app/(overview)/components/ultils/Spinner";
import TextExpander from "@/app/(overview)/components/ultils/TextExpander";
import {useState} from "react";
import {toast} from "sonner";
import {reactPost} from "@/lib/action";
import MoreActionPost from "@/app/(overview)/components/post/MoreActionPost";
import {useRouter} from "next/navigation";
import ImageViewer from "@/app/(overview)/components/ImageViewer";

function Post({postInfo}) {
    const {currentUserId, loading} = useAuth();
    const {
        id,
        avatar,
        username,
        userId,
        content,
        visibility,
        createdAt,
        updatedAt,
        photoResponses,
        numberOfComments,
        numberOfReacts,
        // tagUsers,
        isReacted
    } = postInfo;

    const [isReact, setIsReact] = useState(isReacted);
    const [numOfReacts, setNumOfReacts] = useState(numberOfReacts);
    const router = useRouter();

    if (loading) {
        return <Spinner/>
    }

    async function handleReact() {
        if (isReact === true) {
            setNumOfReacts(numOfReacts - 1);
        } else {
            setNumOfReacts(numOfReacts + 1);
        }
        setIsReact(!isReact);
        console.log("Reacted: ", id);
        const result = await reactPost(id)


        if (!result.isSuccessful) {
            console.log(result.message)
            toast.error("Error while reacting post");
        }
    }

    return (
        <>
            <Card className="w-full">
                <CardHeader>
                    <div className="px-4 flex items-center justify-between">
                        <div className="flex items-center gap-4">
                            <Avatar>
                                <AvatarImage src={avatar}/>
                                <AvatarFallback>{getAvatarFallback(username)}</AvatarFallback>
                            </Avatar>
                            <div className="grid gap-0.5">
                                <Link href={`/home/${userId}`}>
                                    <div className="font-semibold">{username}</div>
                                </Link>
                                <div className="text-xs text-muted-foreground flex items-center gap-2">
                                    <div className="bg-primary rounded-full px-2 py-0.5 text-primary-foreground">
                                        {capitalizeFirstLetter(visibility)}
                                    </div>
                                    <time>{createdAt !== updatedAt ? "Updated" : ""} {`${formatDistanceToNow(updatedAt)} ago`}</time>
                                </div>
                            </div>
                        </div>
                        {currentUserId === String(userId) && (
                            <MoreActionPost postInfo={postInfo}/>
                        )}
                    </div>
                </CardHeader>
                <CardContent>
                    <div className="px-4">
                        <div className="grid gap-4">
                            <TextExpander>{content}</TextExpander>
                            {photoResponses && (
                                <div className="flex justify-center">
                                    <Carousel className="w-full max-w-md">
                                        <CarouselContent>
                                            {photoResponses.map((photo) => (
                                                <CarouselItem key={photo.id || photo.url}>
                                                    <ImageViewer
                                                        src={photo.url}
                                                        alt="Post Image"
                                                        width={1000}
                                                        height={500}
                                                        className="rounded-lg max-h-[500px]"
                                                    />
                                                </CarouselItem>
                                            ))}
                                        </CarouselContent>
                                        <CarouselPrevious/>
                                        <CarouselNext/>
                                    </Carousel>
                                </div>
                            )}
                        </div>
                    </div>
                    <div className="flex items-center justify-between p-4">
                        <div className="flex items-center gap-4">
                            <div className="flex items-center gap-1 text-sm">
                                <Button
                                    variant="ghost"
                                    size="sm"
                                    className="p-0"
                                    onClick={handleReact}
                                >
                                    <Heart
                                        className={`w-4 h-4 ${isReact ? 'fill-current text-black dark:text-white' : 'dark:text-gray-400'}`}/>
                                    {/*<Heart className={`w-4 h-4 ${isReact ? 'fill-current text-black' : ''}`}/>*/}
                                </Button>
                                <Link href={`/react?postId=${id}`}>
                                    <span>{`${numOfReacts} reactions`}</span>
                                </Link>
                            </div>
                            <Link href={`/comment?postId=${id}`}>
                                <div className="flex items-center gap-1 text-sm cursor-pointer">
                                    <MessageCircle className="w-4 h-4"/>
                                    <span>{`${numberOfComments} comments`}</span>
                                </div>
                            </Link>
                        </div>
                        <div className="flex items-center gap-2">
                            <div className="flex items-center gap-1 text-sm">
                                {/*{tagUsers.map(user => {*/}
                                {/*    return (<Card key={user.id} className="p-1">*/}
                                {/*        <Link href={`/home/${user.id}`}>*/}
                                {/*            {`${user.username} `}*/}
                                {/*        </Link>*/}
                                {/*    </Card>)*/}
                                {/*})}*/}
                                <Tag className="w-4 h-4"/>
                            </div>
                        </div>
                    </div>
                </CardContent>
            </Card>
        </>
    )
}

export default Post;