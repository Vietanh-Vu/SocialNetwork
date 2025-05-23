"use server"

import {
    getListFriend,
    getFriendRequest,
    getReceivedFriendRequest,
    getListBlock,
    searchFriend,
    searchUser
} from "@/lib/data";

export async function fetchMoreFriends(page) {
    try {
        const response = await getListFriend(page);
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

export async function fetchMoreFriendRequests(page) {
    try {
        const response = await getFriendRequest(page);
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

export async function fetchMoreReceivedRequests(page) {
    try {
        const response = await getReceivedFriendRequest(page);
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

export async function fetchMoreBlockedUsers(page) {
    try {
        const response = await getListBlock(page);
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

export async function searchMoreFriends(page, query) {
    try {
        const response = await searchFriend(page, query);
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

export async function searchMoreUsers(page, query) {
    try {
        const response = await searchUser(page, query);
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