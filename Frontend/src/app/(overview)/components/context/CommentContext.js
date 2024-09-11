"use client"

import {createContext, useContext, useState} from "react";

const CommentContext = createContext();

function CommentProvider({children}) {
    const [comments, setComments] = useState([]);

    return (
        <CommentContext.Provider value={{comments, setComments}}>
            {children}
        </CommentContext.Provider>
    );
}

function useComment() {
    const context = useContext(CommentContext);
    if (!context) {
        throw new Error("useComment must be used within a CommentProvider");
    }
    return context;
}

export {CommentProvider, useComment};