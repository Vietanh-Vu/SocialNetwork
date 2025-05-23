import {getReceivedFriendRequest} from "@/lib/data";
import SomethingWentWrong from "@/app/(overview)/components/ultils/SomethingWentWrong";
import FriendList from "@/app/(overview)/components/friend/FriendList";
import ScrollToTop from "@/app/(overview)/components/ultils/ScrollToTop";

export default async function Page() {
    const page = 1;
    const result = await getReceivedFriendRequest(page);

    let pageMeta = null;
    let friends = null;

    if (result.isSuccessful) {
        pageMeta = result.data.pageMeta;
        friends = result.data.data;
    }

    return (
        <div className="grid gap-4 pt-6">
            {!result.isSuccessful ? <SomethingWentWrong/> : (
                <FriendList initialFriends={friends} initialPageMeta={pageMeta} type="received"/>
            )}
            <ScrollToTop/>
        </div>
    )
}