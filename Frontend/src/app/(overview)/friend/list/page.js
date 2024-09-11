import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import FriendItem from "@/app/(overview)/components/friend/FriendItem";
import {getFriendRequest, getListFriend} from "@/lib/data";
import ScrollToTop from "@/app/(overview)/components/ultils/ScrollToTop";
import SomethingWentWrong from "@/app/(overview)/components/ultils/SomethingWentWrong";
import FriendList from "@/app/(overview)/components/friend/FriendList";

export default async function Page({searchParams}) {
    const page = searchParams?.page;
    const result = await getListFriend(page);

    let pageMeta = null;
    let friends = null;

    if (result.isSuccessful) {
        pageMeta = result.data.pageMeta;
        friends = result.data.data;
    }

    return (
        <div className="grid gap-4 pt-6">
            {/*<form className="flex items-center gap-4">*/}
            {/*    <Input type="search" placeholder="Search for friends..." className="flex-1"/>*/}
            {/*    <Button type="submit">Search</Button>*/}
            {/*</form>*/}
            {!result.isSuccessful ? <SomethingWentWrong/> : (
                <FriendList initialFriends={friends} initialPageMeta={pageMeta} type="list"/>
            )}
            <ScrollToTop/>
        </div>
    )
}