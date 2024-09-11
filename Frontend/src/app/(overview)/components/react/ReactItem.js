import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import Link from "next/link";
import {getAvatarFallback} from "@/lib/utils";
import {formatDistanceToNow} from "date-fns";

function ReactItem({reactInfo}) {
    const {
        id,
        userId,
        username,
        avatar,
        reactionType,
        createdAt
    } = reactInfo;

    return (
        <div className="grid gap-4 mb-4">
            <div className="flex items-center gap-4 p-4 rounded-lg bg-muted">
                <Avatar>
                    <AvatarImage src={avatar}/>
                    <AvatarFallback>{getAvatarFallback(username)}</AvatarFallback>
                </Avatar>
                <div className="grid gap-1">
                    <Link href={`/home/${userId}`}>
                        <div className="font-medium">{username}</div>
                    </Link>
                    <div className="text-xs text-muted-foreground">
                        <time>{formatDistanceToNow(createdAt)}</time>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ReactItem;