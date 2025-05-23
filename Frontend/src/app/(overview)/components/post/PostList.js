"use client"
import Post from "@/app/(overview)/components/post/Post";
import React, {useState, useEffect} from "react";
import Spinner from "@/app/(overview)/components/ultils/Spinner";
import {usePost} from "@/app/(overview)/components/context/PostContext";
import InfiniteScroll from "@/components/ui/infinite-scroll";
import {fetchMorePosts, fetchMoreNewsFeed} from "@/app/action/postActions";


function PostList({initialUserPost, initialPageMeta, targetUserId, fetchMoreAction = "profile"}) {
    const {userPosts, setUserPosts} = usePost();
    const [pageMeta, setPageMeta] = useState(initialPageMeta);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        setUserPosts(initialUserPost);
        setPageMeta(initialPageMeta);
    }, [initialUserPost, initialPageMeta, setUserPosts]);

    const loadMore = async () => {
        if (pageMeta.hasNext && !isLoading) {
            try {
                setIsLoading(true);
                const nextPage = pageMeta.page + 1;

                // Chọn hàm fetch phù hợp dựa trên loại action
                let result;
                if (fetchMoreAction === "newsfeed") {
                    result = await fetchMoreNewsFeed(nextPage);
                } else {
                    result = await fetchMorePosts(nextPage, targetUserId);
                }

                if (result.isSuccessful) {
                    const {data, pageMeta: newPageMeta} = result.data;

                    setUserPosts(prevPosts => {
                        const newPostIds = new Set(data.map(post => post.id));
                        const uniquePrevPosts = prevPosts.filter(post => !newPostIds.has(post.id));
                        return [...uniquePrevPosts, ...data];
                    });

                    setPageMeta(newPageMeta);
                } else {
                    console.error("Error:", result.message);
                }
            } catch (error) {
                console.error("Error when load posts:", error);
            } finally {
                setIsLoading(false);
            }
        }
    };

    return (
        <InfiniteScroll
            isLoading={isLoading}
            hasMore={pageMeta.hasNext}
            next={loadMore}
            threshold={0.8}
            rootMargin="0px 0px 200px 0px"
        >
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
        </InfiniteScroll>
    )
}

export default PostList;