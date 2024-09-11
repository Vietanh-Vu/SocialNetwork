"use client"
import CommentItem from "@/app/(overview)/components/comment/CommentItem";
import React, {useCallback, useEffect, useState} from "react";
import Spinner from "@/app/(overview)/components/ultils/Spinner";
import {usePathname, useRouter, useSearchParams} from "next/navigation";
import {useComment} from "@/app/(overview)/components/context/CommentContext";

export const revalidate = 1;

function CommentList({initialComments, initialPageMeta}) {
    const {comments, setComments} = useComment();
    const [pageMeta, setPageMeta] = useState(initialPageMeta);
    const [isLoading, setIsLoading] = useState(false);
    const searchParams = useSearchParams();
    const pathname = usePathname();
    const {replace} = useRouter();

    useEffect(() => {
        setComments(prevComments => {
            const newCommentIds = new Set(initialComments.map(comment => comment.commentId));
            const uniquePrevComments = prevComments.filter(comment => !newCommentIds.has(comment.commentId));
            return [...uniquePrevComments, ...initialComments];
        });
        setPageMeta(initialPageMeta);
    }, [initialComments, initialPageMeta, setComments]);

    const loadMore = useCallback(async () => {
        if (pageMeta.hasNext && !isLoading) {
            setIsLoading(true);
            const params = new URLSearchParams(searchParams);
            params.set("page", (pageMeta.page + 1).toString());
            await replace(`${pathname}?${params.toString()}`);
            setIsLoading(false);
        }
    }, [pageMeta, searchParams, pathname, replace, isLoading]);

    return (
        <>
            <div>
                {comments.map(comment => (
                    <CommentItem key={comment.commentId} comment={comment}/>
                ))}
            </div>
            {pageMeta.hasNext && (
                <div className="text-center mt-4">
                    <button
                        onClick={loadMore}
                        className="text-primary font-semibold hover:underline"
                    >
                        Load more
                    </button>
                </div>
            )}
            {isLoading && (
                <div className="mt-4 text-center">
                    <Spinner/>
                </div>
            )}
        </>
    );
}

export default CommentList;