import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {Button} from "@/components/ui/button";
import {getAvatarFallback} from "@/lib/utils";
import Link from "next/link";
import SendRequestButton from "@/app/(overview)/components/friend/button/SendRequestButton";

function SuggestFriendItem({suggestFriendInfo}) {
    const {id, username, avatar, mutualFriends} = suggestFriendInfo;

    return (
        <div className="flex items-center justify-around gap-6 mb-4">
            <div className="flex items-center gap-2">
                <Avatar className="h-10 w-10">
                    <AvatarImage src={avatar}/>
                    <AvatarFallback>{getAvatarFallback(username)}</AvatarFallback>
                </Avatar>
                <div>
                    <Link href={`/home/${id}`}>
                        <h5 className="font-medium">{username}</h5>
                    </Link>
                    <p className="text-muted-foreground text-sm">{`${mutualFriends} mutual friend`}</p>
                </div>
            </div>
            <SendRequestButton userId={id}/>
        </div>
    )
}

export default SuggestFriendItem;