"use client"

import {useEffect, useState} from "react";
import FriendItem from "@/app/(overview)/components/friend/FriendItem";
import Spinner from "@/app/(overview)/components/ultils/Spinner";
import RejectButton from "@/app/(overview)/components/friend/button/RejectButton";
import UnFriendButton from "@/app/(overview)/components/friend/button/UnFriendButton";
import UnBlockButton from "@/app/(overview)/components/friend/button/UnBlockButton";
import SendRequestButton from "@/app/(overview)/components/friend/button/SendRequestButton";
import DeleteRequestButton from "@/app/(overview)/components/friend/button/DeleteRequestButton";
import AcceptButton from "@/app/(overview)/components/friend/button/AcceptButton";
import BlockButton from "@/app/(overview)/components/friend/button/BlockButton";
import CloseRelationButton from "@/app/(overview)/components/friend/button/CloseRelationButton";
import InfiniteScroll from "@/components/ui/infinite-scroll";
import {
    fetchMoreBlockedUsers,
    fetchMoreFriendRequests,
    fetchMoreFriends,
    fetchMoreReceivedRequests, searchMoreFriends, searchMoreUsers
} from "@/app/action/friendAction";

function FriendList({initialFriends, initialPageMeta, type, searchQuery}) {
    const [friends, setFriends] = useState(initialFriends);
    const [pageMeta, setPageMeta] = useState(initialPageMeta);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        setFriends(initialFriends);
        setPageMeta(initialPageMeta);
    }, [initialFriends, initialPageMeta]);

    const loadMore = async () => {
        if (pageMeta.hasNext && !isLoading) {
            try {
                setIsLoading(true);
                const nextPage = pageMeta.page + 1;

                // Chọn hàm fetch phù hợp dựa trên loại danh sách
                let result;
                switch (type) {
                    case "list":
                        result = await fetchMoreFriends(nextPage);
                        break;
                    case "request":
                        result = await fetchMoreFriendRequests(nextPage);
                        break;
                    case "received":
                        result = await fetchMoreReceivedRequests(nextPage);
                        break;
                    case "block":
                        result = await fetchMoreBlockedUsers(nextPage);
                        break;
                    case "search":
                        result = await searchMoreUsers(nextPage, searchQuery);
                        break;
                    case "friend-search":
                        result = await searchMoreFriends(nextPage, searchQuery);
                        break;
                    default:
                        result = await fetchMoreFriends(nextPage);
                }

                if (result.isSuccessful) {
                    const {data, pageMeta: newPageMeta} = result.data;

                    setFriends(prevFriends => {
                        const newFriendIds = new Set(data.map(friend => friend.id));
                        const uniquePrevFriends = prevFriends.filter(friend => !newFriendIds.has(friend.id));
                        return [...uniquePrevFriends, ...data];
                    });

                    setPageMeta(newPageMeta);
                }
            } catch (error) {
                console.error("Error when loading friends:", error);
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
                {friends.map(friend => (
                    <FriendItem key={friend.id} friendInfo={friend}>
                        {type === "request" && <DeleteRequestButton userId={friend.id}/>}

                        {type === "received" && (
                            <div className="flex gap-2">
                                <AcceptButton userId={friend.id}/>
                                <RejectButton userId={friend.id}/>
                            </div>
                        )}

                        {type === "list" && (
                            <div className="flex gap-2">
                                <CloseRelationButton userId={friend.id} closeRelationship={friend.closeRelationship}/>
                                <UnFriendButton userId={friend.id}/>
                                <BlockButton userId={friend.id}/>
                            </div>
                        )}

                        {type === "block" && <UnBlockButton userId={friend.id}/>}

                        {(type === "search" || type === "friend-search") && (
                            <div className="flex gap-2">
                                {friend.status === "FRIEND" && (
                                    <div className="flex gap-2">
                                        <CloseRelationButton userId={friend.id}
                                                             closeRelationship={friend.closeRelationship}/>
                                        <UnFriendButton userId={friend.id}/>
                                        <BlockButton userId={friend.id}/>
                                    </div>
                                )}
                                {friend.status === "BLOCK" && <UnBlockButton userId={friend.id}/>}
                                {friend.status === "REQUESTING" && <DeleteRequestButton userId={friend.id}/>}
                                {friend.status === "RECEIVED" && (
                                    <div className="flex gap-2">
                                        <AcceptButton userId={friend.id}/>
                                        <RejectButton userId={friend.id}/>
                                    </div>
                                )}
                                {friend.status === null && (
                                    <div className="flex gap-2">
                                        <SendRequestButton userId={friend.id}/>
                                        <BlockButton userId={friend.id}/>
                                    </div>
                                )}
                            </div>
                        )}
                    </FriendItem>
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

export default FriendList;