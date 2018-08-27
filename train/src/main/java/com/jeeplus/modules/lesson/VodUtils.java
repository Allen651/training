package com.jeeplus.modules.lesson;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadImageRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadImageResponse;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
/**
 * Vod视频管理工具类
 * @author wangqy
 * @version 2018-02-09 14:26
 */
public class VodUtils {
	public static String accessKeyId = "LTAISMvJeQ3iIsXo";
	public static String accessKeySecret = "dVJC6kDNiVjSS0AYk0cbvhCwfrGF0V";

	public static CreateUploadVideoResponse createUploadVideo(DefaultAcsClient client,String filename) {
		CreateUploadVideoRequest request = new CreateUploadVideoRequest();
		CreateUploadVideoResponse response = null;
		try {
			/*
			 * 必选，视频源文件名称（必须带后缀, 支持 ".3gp", ".asf", ".avi", ".dat", ".dv", ".flv", ".f4v",
			 * ".gif", ".m2t", ".m3u8", ".m4v", ".mj2", ".mjpeg", ".mkv", ".mov", ".mp4",
			 * ".mpe", ".mpg", ".mpeg", ".mts", ".ogg", ".qt", ".rm", ".rmvb", ".swf",
			 * ".ts", ".vob", ".wmv", ".webm"".aac", ".ac3", ".acm", ".amr", ".ape", ".caf",
			 * ".flac", ".m4a", ".mp3", ".ra", ".wav", ".wma"）
			 */
			String title = filename.substring(0,filename.lastIndexOf("."));
			request.setFileName(filename);
			// 必选，视频标题
			request.setTitle(title);
			// 可选，分类ID
			request.setCateId(0);
			// 可选，视频标签，多个用逗号分隔
			request.setTags("标签1,标签2");
			// 可选，视频描述
			request.setDescription("视频描述");
			response = client.getAcsResponse(request);
		} catch (ServerException e) {
			System.out.println("CreateUploadVideoRequest Server Exception:");
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			System.out.println("CreateUploadVideoRequest Client Exception:");
			e.printStackTrace();
			return null;
		}
		System.out.println("RequestId:" + response.getRequestId());
		System.out.println("UploadAuth:" + response.getUploadAuth());
		System.out.println("UploadAddress:" + response.getUploadAddress());
		return response;
	}

	private static void refreshUploadVideo(DefaultAcsClient client, String videoId) {
		RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
		RefreshUploadVideoResponse response = null;
		try {
			request.setVideoId(videoId);
			response = client.getAcsResponse(request);
		} catch (ServerException e) {
			System.out.println("RefreshUploadVideoRequest Server Exception:");
			e.printStackTrace();
			return;
		} catch (ClientException e) {
			System.out.println("RefreshUploadVideoRequest Client Exception:");
			e.printStackTrace();
			return;
		}
		System.out.println("RequestId:" + response.getRequestId());
		System.out.println("UploadAuth:" + response.getUploadAuth());
	}
	
	public static GetVideoPlayAuthResponse getVideoPlayAuth(DefaultAcsClient client,String videoId) {
	    GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
	    request.setVideoId(videoId);
	    GetVideoPlayAuthResponse response = null;
	    try {
	        response = client.getAcsResponse(request);
	    } catch (ServerException e) {
	        throw new RuntimeException("GetVideoPlayAuthRequest Server failed");
	    } catch (ClientException e) {
	        throw new RuntimeException("GetVideoPlayAuthRequest Client failed");
	    }
	    response.getPlayAuth();              //播放凭证
	    response.getVideoMeta();             //视频Meta信息
	    return response;
	}
	
	
	public static CreateUploadImageResponse createUploadImage(DefaultAcsClient client) {
		CreateUploadImageRequest request = new CreateUploadImageRequest();
		CreateUploadImageResponse response = null;
		try {
			//必选, 视频封面
			request.setImageType("cover");
			// 必选，视频标题
			request.setTitle("图片标题");
			//可选 ， 默认png
			request.setImageExt("png");
			// 可选，图片标签，多个用逗号分隔
			request.setTags("标签1,标签2");
			response = client.getAcsResponse(request);
		} catch (ServerException e) {
			System.out.println("CreateUploadVideoRequest Server Exception:");
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			System.out.println("CreateUploadVideoRequest Client Exception:");
			e.printStackTrace();
			return null;
		}
		System.out.println("RequestId:" + response.getRequestId());
		System.out.println("UploadAuth:" + response.getUploadAuth());
		System.out.println("UploadAddress:" + response.getUploadAddress());
		System.out.println("ImageURL:" + response.getImageURL());
		System.out.println("ImageId:" + response.getImageId());
		return response;
	}

	public static void main(String[] args) {
		 //DefaultAcsClient aliyunClient= new DefaultAcsClient(DefaultProfile.getProfile("cn-shanghai",accessKeyId,accessKeySecret));
		 //CreateUploadVideoResponse videoId = createUploadVideo(aliyunClient);
		 //System.out.println("VideoId:" + videoId.getVideoId());
		 //当网络异常导致文件上传失败时,可刷新上传凭证后再次执行上传操作
		 //refreshUploadVideo(aliyunClient, videoId.getVideoId());
		 //GetVideoPlayAuthResponse respone=getVideoPlayAuth(aliyunClient,"7ba541452758438581859b8b12b44f95");
		 //System.out.println(respone.getPlayAuth());
		 //System.out.println(respone.getVideoMeta());
	}

}
