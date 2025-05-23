import {Card, CardContent, CardHeader} from "@/components/ui/card";
import SomethingWentWrong from "@/app/(overview)/components/ultils/SomethingWentWrong";
import {getNewsFeed} from "@/lib/data";
import PostList from "@/app/(overview)/components/post/PostList";
import ScrollToTop from "@/app/(overview)/components/ultils/ScrollToTop";


export default async function Page({searchParams}) {
    // Đảm bảo page là số nguyên và mặc định là 1
    const page = parseInt(searchParams?.page) || 1;
    const pageSize = 10; // Số bài viết mỗi trang, phù hợp với backend

    const result = await getNewsFeed(page, pageSize);

    return (
        <>
            <Card>
                <CardHeader>
                    <div className="flex items-center justify-between">
                        <h2 className="font-semibold">Newsfeed</h2>
                    </div>
                </CardHeader>
                <CardContent className="grid gap-6">
                    {!result.isSuccessful ? (
                        <SomethingWentWrong/>
                    ) : (
                        <PostList
                            initialUserPost={result.data.data}
                            initialPageMeta={result.data.pageMeta}
                            fetchMoreAction="newsfeed"
                        />
                    )}
                </CardContent>
            </Card>
            <ScrollToTop/>
        </>
    )
}