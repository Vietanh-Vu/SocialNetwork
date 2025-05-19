import { NextResponse } from 'next/server';
import axios from 'axios';
import { cookies } from 'next/headers';

export async function GET(request) {
    const searchParams = new URL(request.url).searchParams;
    const minProbability = searchParams.get('minProbability');
    const maxProbability = searchParams.get('maxProbability');

    try {
        // Lấy token từ cookies
        const accessToken = cookies().get("access-token")?.value;

        // Tạo một instance axios mới không sử dụng interceptors
        const baseURL = process.env.NEXT_PUBLIC_API_URL;

        // Thực hiện yêu cầu trực tiếp không qua http client với interceptors
        const response = await axios({
            method: 'get',
            url: `${baseURL}/admin/problematic-comments/export`,
            params: { minProbability, maxProbability },
            responseType: 'arraybuffer',
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        });

        // Lấy filename từ header
        const contentDisposition = response.headers['content-disposition'];
        let filename = 'problematic_comments.xlsx';
        if (contentDisposition) {
            const filenameMatch = contentDisposition.match(/filename="?([^"]+)"?/);
            if (filenameMatch && filenameMatch[1]) {
                filename = filenameMatch[1];
            }
        }

        // Trả về dữ liệu nhị phân
        return new NextResponse(response.data, {
            status: 200,
            headers: {
                'Content-Type': response.headers['content-type'],
                'Content-Disposition': `attachment; filename="${filename}"`,
            },
        });
    } catch (error) {
        console.error("Download error:", error);
        return new NextResponse(JSON.stringify({ error: error.message }), {
            status: 500,
            headers: {
                'Content-Type': 'application/json',
            },
        });
    }
}