import {Card, CardContent, CardHeader} from "@/components/ui/card";
import SomethingWentWrong from "@/app/(overview)/components/ultils/SomethingWentWrong";
import {getNewsFeed} from "@/lib/data";
import PostList from "@/app/(overview)/components/post/PostList";
import ScrollToTop from "@/app/(overview)/components/ultils/ScrollToTop";


export default async function Page({searchParams}) {
    const page = searchParams?.page;
    const result = await getNewsFeed(page);
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
                        <h2 className="font-semibold">Newsfeed</h2>
                    </div>
                </CardHeader>
                <CardContent className="grid gap-6">
                    {!result.isSuccessful ? <SomethingWentWrong/> :
                        <PostList initialUserPost={userPost} initialPageMeta={pageMeta}/>}
                </CardContent>
            </Card>
            <ScrollToTop/>
        </>
    )
}