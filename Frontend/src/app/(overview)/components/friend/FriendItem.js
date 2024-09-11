import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {getAvatarFallback} from "@/lib/utils";
import Link from "next/link";
import {Users} from "lucide-react";


function FriendItem({friendInfo, children}) {
    const {closeRelationship, avatar, email, id, status, username, mutualFriends} = friendInfo;
    return (
        <div className="flex items-center justify-between bg-muted p-4 rounded-lg">
            <div className="flex items-center gap-4">
                <Avatar className="w-12 h-12">
                    <AvatarImage src={avatar} alt="User Avatar"/>
                    <AvatarFallback>{getAvatarFallback(username)}</AvatarFallback>
                </Avatar>
                <div>
                    <Link href={`/home/${id}`}>
                        <div className="font-medium">{username}</div>
                    </Link>
                    <div className="text-sm text-muted-foreground">
                        <div className="flex gap-2 items-center">
                            <Users className="w-3 h-3"/> {mutualFriends} mutual friends
                        </div>
                    </div>
                </div>
            </div>
            {children}
        </div>
    )
}

export default FriendItem;