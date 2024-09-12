"use client"

import {useCallback, useEffect, useState} from "react";
import {usePathname, useRouter, useSearchParams} from "next/navigation";
import Spinner from "@/app/(overview)/components/ultils/Spinner";
import FriendItem from "@/app/(overview)/components/friend/FriendItem";
import RejectButton from "@/app/(overview)/components/friend/button/RejectButton";
import UnFriendButton from "@/app/(overview)/components/friend/button/UnFriendButton";
import UnBlockButton from "@/app/(overview)/components/friend/button/UnBlockButton";
import SendRequestButton from "@/app/(overview)/components/friend/button/SendRequestButton";
import DeleteRequestButton from "@/app/(overview)/components/friend/button/DeleteRequestButton";
import AcceptButton from "@/app/(overview)/components/friend/button/AcceptButton";
import BlockButton from "@/app/(overview)/components/friend/button/BlockButton";
import CloseRelationButton from "@/app/(overview)/components/friend/button/CloseRelationButton";

function FriendList({initialFriends, initialPageMeta, type}) {
    const [friends, setFriends] = useState(initialFriends);
    const [pageMeta, setPageMeta] = useState(initialPageMeta);
    const [isLoading, setIsLoading] = useState(false); // Loading state
    const searchParams = useSearchParams();
    const pathname = usePathname();
    const {replace} = useRouter();

    useEffect(() => {
        if (initialPageMeta.page === 1) {
            setFriends(initialFriends);
        } else {
            setFriends(prevFriends => {
                const newFriendIds = new Set(initialFriends.map(friend => friend.id));
                const uniquePrevFriends = prevFriends.filter(friend => !newFriendIds.has(friend.id));
                return [...uniquePrevFriends, ...initialFriends];
            });
        }
        setPageMeta(initialPageMeta);
    }, [initialFriends, initialPageMeta, setFriends]);

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
        <div>
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

                        {type === "search" && (
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
        </div>
    )
}

export default FriendList;