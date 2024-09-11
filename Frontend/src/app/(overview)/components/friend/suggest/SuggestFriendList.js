"use client"
import React, {useState, useEffect} from 'react';
import {Card, CardContent, CardFooter, CardHeader} from "@/components/ui/card";
import SuggestFriendItem from "@/app/(overview)/components/friend/suggest/SuggestFriendItem";
import {ScrollArea} from "@/components/ui/scroll-area";
import {getSuggestFriend} from "@/lib/data";
import SomethingWentWrong from "@/app/(overview)/components/ultils/SomethingWentWrong";
import Spinner from "@/app/(overview)/components/ultils/Spinner";

const SuggestFriendList = () => {
    const [suggestFriends, setSuggestFriends] = useState([]);
    const [pageMeta, setPageMeta] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchFriends = async (page = 1) => {
        setIsLoading(true);
        try {
            const res = await getSuggestFriend(page);
            if (res.isSuccessful) {
                if (page === 1) {
                    setSuggestFriends(res.data.data);
                } else {
                    setSuggestFriends(prevFriends => [...prevFriends, ...res.data.data]);
                }
                setPageMeta(res.data.pageMeta);
            } else {
                setError("Failed to fetch data");
            }
        } catch (err) {
            setError("An error occurred while fetching data");
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchFriends().then(r => {
        });
    }, []);

    const loadMore = () => {
        if (pageMeta && pageMeta.hasNext) {
            fetchFriends(pageMeta.page + 1);
        }
    };

    if (error) return (
        <Card>
            <SomethingWentWrong/>
        </Card>
    );

    return (
        <Card>
            <CardHeader>
                <h4 className="font-semibold">Suggested Friends</h4>
            </CardHeader>
            <CardContent className="grid gap-4 max-h-96">
                <ScrollArea className="max-h-96">
                    {suggestFriends.map((friend) => (
                        <SuggestFriendItem key={friend.id} suggestFriendInfo={friend}/>
                    ))}
                    {isLoading && <Spinner/>}
                    {pageMeta && pageMeta.hasNext && (
                        <div className="text-center mt-4">
                            <button
                                onClick={loadMore}
                                className="text-primary font-semibold hover:underline"
                                disabled={isLoading}
                            >
                                {isLoading ? <Spinner/> : 'Load more'}
                            </button>
                        </div>
                    )}
                </ScrollArea>
            </CardContent>
            <CardFooter></CardFooter>
        </Card>
    );
};

export default SuggestFriendList;