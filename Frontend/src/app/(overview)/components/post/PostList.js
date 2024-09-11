"use client"
import Post from "@/app/(overview)/components/post/Post";
import {usePathname, useRouter, useSearchParams} from "next/navigation";
import React, {useState, useEffect, useCallback} from "react";
import Spinner from "@/app/(overview)/components/ultils/Spinner";
import {usePost} from "@/app/(overview)/components/context/PostContext";

function PostList({initialUserPost, initialPageMeta}) {
    const {userPosts, setUserPosts} = usePost();
    // const [userPosts, setUserPosts] = useState(initialUserPost);
    const [pageMeta, setPageMeta] = useState(initialPageMeta);
    const [isLoading, setIsLoading] = useState(false); // Loading state
    const searchParams = useSearchParams();
    const pathname = usePathname();
    const {replace} = useRouter();

    useEffect(() => {
        if (initialPageMeta.page === 1) {
            setUserPosts(initialUserPost);
        } else {
            setUserPosts(prevPosts => {
                const newPostIds = new Set(initialUserPost.map(post => post.id));
                const uniquePrevPosts = prevPosts.filter(post => !newPostIds.has(post.id));
                return [...uniquePrevPosts, ...initialUserPost];
            });
        }
        setPageMeta(initialPageMeta);
    }, [initialUserPost, initialPageMeta, setUserPosts]);

    const loadMore = useCallback(async () => {
        if (pageMeta.hasNext && !isLoading) {
            setIsLoading(true); // Start loading
            const params = new URLSearchParams(searchParams);
            params.set("page", (pageMeta.page + 1).toString());
            await replace(`${pathname}?${params.toString()}`, {scroll: false});
            setIsLoading(false); // Stop loading
        }
    }, [pageMeta, searchParams, pathname, replace, isLoading]);

    const handleScroll = useCallback(() => {
        if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight - 500) {
            loadMore();
        }
    }, [loadMore]);

    useEffect(() => {
        window.addEventListener("scroll", handleScroll);
        return () => window.removeEventListener("scroll", handleScroll);
    }, [handleScroll]);

    return (
        <>
            <div className="grid gap-6">
                {userPosts.map(post => (
                    <Post key={post.id} postInfo={post}/>
                ))}
            </div>
            {isLoading && (
                <div className="mt-4 text-center">
                    <Spinner/>
                </div>
            )}
        </>
    )
}

export default PostList;
