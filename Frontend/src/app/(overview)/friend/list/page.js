// Frontend/src/app/(overview)/friend/list/page.js
import Search from "@/app/(overview)/components/ultils/Search";
import SomethingWentWrong from "@/app/(overview)/components/ultils/SomethingWentWrong";
import FriendList from "@/app/(overview)/components/friend/FriendList";
import ScrollToTop from "@/app/(overview)/components/ultils/ScrollToTop";
import {getListFriend, searchFriend} from "@/lib/data";

export default async function Page({searchParams}) {
    const page = 1;
    const query = searchParams?.query || '';

    // Nếu có từ khóa tìm kiếm, gọi hàm searchFriend, nếu không thì lấy toàn bộ danh sách bạn bè
    const result = query
        ? await searchFriend(page, query)
        : await getListFriend(page);

    let pageMeta = null;
    let friends = null;

    if (result.isSuccessful) {
        pageMeta = result.data.pageMeta;
        friends = result.data.data;
    }

    return (
        <div className="flex flex-col gap-4 pt-6">
            <Search searchFor={"friend"}/>
            <div className="grid gap-4">
                {!result.isSuccessful ? <SomethingWentWrong/> : (
                    <FriendList
                        initialFriends={friends}
                        initialPageMeta={pageMeta}
                        type={query ? "friend-search" : "list"}
                        searchQuery={query}
                    />
                )}
                <ScrollToTop/>
            </div>
        </div>
    )
}