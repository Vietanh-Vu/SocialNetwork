"use client"

import {createContext, useContext, useState} from "react";

const ReactionContext = createContext();

function ReactionProvider({children}) {
    const [reactions, setReactions] = useState([]);

    return (
        <ReactionContext.Provider value={{reactions, setReactions}}>
            {children}
        </ReactionContext.Provider>
    );
}

function useReaction() {
    const context = useContext(ReactionContext);
    if (!context) {
        throw new Error("useReaction must be used within a ReactionProvider");
    }
    return context;
}

export {ReactionProvider, useReaction}