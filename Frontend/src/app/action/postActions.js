"use server"

import {getUserPost, getNewsFeed} from "@/lib/data";

export async function fetchMorePosts(page, userId) {
    try {
        const response = await getUserPost(page, userId);
        return {
            isSuccessful: true,
            data: response.data
        };
    } catch (error) {
        return {
            isSuccessful: false,
            message: error.response?.data?.message || "An error occurred"
        };
    }
}

export async function fetchMoreNewsFeed(page) {
    try {
        const response = await getNewsFeed(page);
        return {
            isSuccessful: true,
            data: response.data
        };
    } catch (error) {
        return {
            isSuccessful: false,
            message: error.response?.data?.message || "An error occurred"
        };
    }
}