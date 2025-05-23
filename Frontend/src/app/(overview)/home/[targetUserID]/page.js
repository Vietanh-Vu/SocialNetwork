import {Card, CardContent, CardHeader} from "@/components/ui/card";
import {getUserPost, getUserProfile} from "@/lib/data";
import NotAllow from "@/app/(overview)/components/ultils/NotAllow";
import UserProfile from "@/app/(overview)/components/user/UserProfile";
import PostList from "@/app/(overview)/components/post/PostList";
import ScrollToTop from "@/app/(overview)/components/ultils/ScrollToTop";

export default async function Page({params, searchParams}) {
    const targetUserId = params.targetUserID;
    const page = searchParams?.page;
    const userProfile = await getUserProfile(targetUserId);
    const result = await getUserPost(page, targetUserId);

    let pageMeta = null;
    let userPost = null;

    if (result.isSuccessful) {
        pageMeta = result.data.pageMeta;
        userPost = result.data.data;
    }


    return (
        <>
            <Card>
                <CardHeader>
                    <div className="flex items-center justify-between">
                        <h4 className="font-semibold">Personal Information</h4>
                    </div>
                </CardHeader>
                {userProfile.isAllowed ? <UserProfile userProfile={userProfile.data}/> :
                    <NotAllow message={userProfile.message}/>}
            </Card>
            <Card>
                <CardHeader>
                    <div className="flex items-center justify-between">
                        <h4 className="font-semibold">Post</h4>
                    </div>
                </CardHeader>
                <CardContent className="grid gap-6">
                    {result.isSuccessful ?
                        <PostList initialPageMeta={pageMeta} initialUserPost={userPost} targetUserId={targetUserId} fetchMoreAction={"profile"}/> :
                        <NotAllow message={"You are not allowed to see this user post"}/>}
                </CardContent>
            </Card>
            <ScrollToTop/>
        </>
    )
}