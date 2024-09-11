"use client";
import {DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger} from "@/components/ui/dropdown-menu";

import {useState} from "react";
import {deleteCloseRelation, setCloseRelation} from "@/lib/action";
import {toast} from "sonner";
import {Button} from "@/components/ui/button";

function CloseRelationButton({userId, closeRelationship}) {
    const [relation, setRelation] = useState(closeRelationship === null ? "FRIEND" : closeRelationship)
    const handleRelationChange = async (newRelation) => {
        setRelation(newRelation)
        console.log(newRelation)
        if (newRelation === "FRIEND") {
            const result = await deleteCloseRelation(userId)
            if (result.isSuccessful) {
                toast.success("Delete close relationship successfully")
            } else {
                toast.error("Delete close relationship failed")
            }

            return
        }
        const result = await setCloseRelation(userId, newRelation)
        if (result.isSuccessful) {
            toast.success("Set close relationship successfully")
        } else {
            toast.error("Set close relationship failed")
        }
    }

    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
                <Button variant="ghost" size="sm" onClick={() => handleRelationChange(relation)}>
                    <span className="capitalize">{relation}</span>
                </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
                <DropdownMenuItem>
                    <Button variant="ghost" size="sm" onClick={() => handleRelationChange("FRIEND")}>
                        FRIEND
                    </Button>
                </DropdownMenuItem>
                <DropdownMenuItem>
                    <Button variant="ghost" size="sm" onClick={() => handleRelationChange("FATHER")}>
                        FATHER
                    </Button>
                </DropdownMenuItem>
                <DropdownMenuItem>
                    <Button variant="ghost" size="sm" onClick={() => handleRelationChange("MOTHER")}>
                        MOTHER
                    </Button>
                </DropdownMenuItem>
                <DropdownMenuItem>
                    <Button variant="ghost" size="sm" onClick={() => handleRelationChange("BROTHER")}>
                        BROTHER
                    </Button>
                </DropdownMenuItem>
                <DropdownMenuItem>
                    <Button variant="ghost" size="sm" onClick={() => handleRelationChange("SISTER")}>
                        SISTER
                    </Button>
                </DropdownMenuItem>
                <DropdownMenuItem>
                    <Button variant="ghost" size="sm" onClick={() => handleRelationChange("DATING")}>
                        DATING
                    </Button>
                </DropdownMenuItem>
            </DropdownMenuContent>
        </DropdownMenu>
    )
}

export default CloseRelationButton;