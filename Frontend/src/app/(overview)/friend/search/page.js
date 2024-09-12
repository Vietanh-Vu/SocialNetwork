import Search from "@/app/(overview)/components/ultils/Search";
import SomethingWentWrong from "@/app/(overview)/components/ultils/SomethingWentWrong";
import FriendList from "@/app/(overview)/components/friend/FriendList";
import ScrollToTop from "@/app/(overview)/components/ultils/ScrollToTop";
import {searchUser} from "@/lib/data";

export default async function Page({searchParams}) {
    const query = searchParams?.query || '';

    const page = searchParams?.page;
    const result = await searchUser(page, query);

    let pageMeta = null;
    let friends = null;

    if (result.isSuccessful) {
        pageMeta = result.data.pageMeta;
        friends = result.data.data;
    }

    return (
        <div className="flex flex-col gap-4 pt-6">
            <Search/>
            <div className="grid gap-4">
                {!result.isSuccessful ? <SomethingWentWrong/> : (
                    <FriendList initialFriends={friends} initialPageMeta={pageMeta} type="search"/>
                )}
                <ScrollToTop/>
            </div>
        </div>
    );
}