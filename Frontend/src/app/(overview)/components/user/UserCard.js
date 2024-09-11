import {Card, CardContent, CardHeader} from "@/components/ui/card";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {getAvatarFallback} from "@/lib/utils";
import TextExpander from "@/app/(overview)/components/ultils/TextExpander";
import {getUserFriendCount, getUserPostCount, getUserProfile} from "@/lib/data";
import Spinner from "@/app/(overview)/components/ultils/Spinner";
import SomethingWentWrong from "@/app/(overview)/components/ultils/SomethingWentWrong";

async function UserCard() {

    const postCountResult = await getUserPostCount();
    const friendCountResult = await getUserFriendCount();
    const {_, data: userInfo} = await getUserProfile();

    if (!userInfo) {
        return (
            <Card>
                <SomethingWentWrong/>
            </Card>
        );
    }

    return (
        <Card>
            <CardHeader>
                <div className="flex items-center gap-4 mb-4">
                    <Avatar className="h-12 w-12">
                        <AvatarImage src={userInfo.avatar}/>
                        <AvatarFallback>{getAvatarFallback(userInfo.username)}</AvatarFallback>
                    </Avatar>
                    <div>
                        <h4 className="font-semibold">{userInfo.username}</h4>
                    </div>
                </div>
                {userInfo.bio ? (<TextExpander>{userInfo.bio}</TextExpander>) : ""}
            </CardHeader>
            <CardContent>
                <div className="grid gap-4">
                    <div className="grid gap-1">
                        <div className="text-sm font-medium">Friends</div>
                        <div className="text-2xl font-bold">{friendCountResult.data}</div>
                    </div>
                    <div className="grid gap-1">
                        <div className="text-sm font-medium">Posts</div>
                        <div className="text-2xl font-bold">{postCountResult.data}</div>
                    </div>
                </div>
            </CardContent>
        </Card>
    )
}

export default UserCard;
