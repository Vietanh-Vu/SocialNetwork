"use client"

import {createContext, useContext, useState} from "react";

const PostContext = createContext();

function PostProvider({children}) {
    const [userPosts, setUserPosts] = useState([]);

    return (
        <PostContext.Provider value={{userPosts, setUserPosts}}>
            {children}
        </PostContext.Provider>
    );
}

function usePost() {
    const context = useContext(PostContext);
    if (!context) {
        throw new Error("usePost must be used within a PostProvider");
    }
    return context;
}

export {PostProvider, usePost};