package com.newroad.fileext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.newroad.pilot.PilotOssEx;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/spring-mvc.xml" })
public class COSDaoTest {

	// private static final SPLogger LOG = SPLogger.getLogger(COSDaoTest.class);

	@Value("${COS_APP_ID}")
	private String appID;

	@Value("${COS_DEV_ID}")
	private String devID;

	@Value("${COS_DEV_KEY}")
	private String devKey;

	private String realm = "supernote.lenovo.com";

	private String lenovoST = "ZAgAAAAAAAGE9MTAwMTQyMjIyOTUmYj0xJmM9MSZkPTEwNjQyJmU9MkQwQjQ0QTA5RTdBODg0MDJCNDJDNUY4Rjk4Rjc0QTYxJmg9MTM4NjQ4NjI4Mzk3MiZpPTQzMjAwJmo9MTM4NjQ4NjI4M7wDqiF9tQzjoMF2JzcuUZM=";

	@Value("${COS_DEV_SPEC}")
	private String spec;

	@Value("${COS_DEV_WORKSPACE}")
	private String workspace;

	private PilotOssEx svr;

	private String connector = "http://iam.lenovows.com/v1/connector/0040478284cdb800";

	private static ExecutorService threadPoolTaskExecutor = Executors
			.newCachedThreadPool();

	@Test
	@Ignore
	public void begin() {
		// PilotOssCloud.setTestEnvironment();
		// svr = PilotOssCloud.CreateOssCloudEx();
		// try {
		// svr.loginByLenovoID(
		// appID,
		// devID,
		// devKey,
		// realm,
		// lenovoST,
		// "", workspace);
		// connector = svr.getConnector();
		// System.out.println("Connector:" + connector);
		// } catch (PilotException e) {
		// e.printStackTrace();
		// throw new RuntimeException("登录失败");
		// }
	}

//	@Test
//	@Ignore
//	public void getFile() throws Exception {
//		FileData fileData = new FileData();
//		fileData.setBucketName("lenote");
//		fileData.setKeyId("00405e7063080100");
//		fileData.setFileCachePath("D:\\supernote\\key1.jpg");
//		fileData.setSize(777835L);
//		COSExecutionTask costask = new COSExecutionTask(
//				COSExecutionSignal.GET_OBJECT, connector, null, fileData);
//		Future<Object> future = threadPoolTaskExecutor.submit(costask);
//		try {
//			@SuppressWarnings("unused")
//			Object returnObj = future.get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	@Ignore
//	public void putFile() throws Exception {
//		JSONObject sessionUser = new JSONObject();
//		sessionUser.put("cosConnector", connector);
//
//		FileData cdata = new FileData();
//		cdata.setBucketName("lenote");
//		cdata.setOriginalFileName("test1");
//		File testFile = new File(
//				"D:\\supernote\\a7223e57-e8f2-4069-9f57-83837a8165c6.apk");
//		byte[] fileByte = TypeConvertor.inputStream2byte(new FileInputStream(
//				testFile));
//		cdata.setFileByte(fileByte);
//		cdata.setContentType("application/octet-stream");
//		cdata.setSize(3779246L);
//
//		COSExecutionTask costask = new COSExecutionTask(
//				COSExecutionSignal.PUT_OBJECT, connector, null, cdata);
//		Future<Object> future = threadPoolTaskExecutor.submit(costask);
//		try {
//			@SuppressWarnings("unused")
//			Object returnObj = future.get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	@Test
//	@Ignore
//	public void uploadDownloadFile() throws Exception {
//		JSONObject sessionUser = new JSONObject();
//		sessionUser.put("cosConnector", connector);
//
//		FileData cdata = new FileData();
//		cdata.setBucketName("lenote");
//		cdata.setKeyId("key6");
//		cdata.setOriginalFileName("test1");
//		File testFile = new File("D:\\supernote\\disk2\\2013\\12_6\\3\\2.jpg");
//		cdata.setContentType("application/octet-stream");
//		cdata.setSize(testFile.length());
//		cdata.setFileCachePath("D:\\supernote\\disk2\\2013\\12_6\\3\\2.jpg");
//
//		COSExecutionTask costask = new COSExecutionTask(
//				COSExecutionSignal.PUT_OBJECT, connector, null, cdata);
//		Future<Object> future = threadPoolTaskExecutor.submit(costask);
//		FileData returnObj = (FileData) future.get();
//		returnObj.setFileCachePath("D:\\supernote\\return2.jpg");
//
//		COSExecutionTask costask2 = new COSExecutionTask(
//				COSExecutionSignal.GET_OBJECT, connector, null, returnObj);
//		Future<Object> future2 = threadPoolTaskExecutor.submit(costask2);
//		FileData returnObj2 = (FileData) future2.get();
//		System.out.println("Object key:" + returnObj2.getKeyId());
//	}
//
//	@Test
//	@Ignore
//	public void batchCreatePublicLink() {
//		FileData[] dataArray = new FileData[2];
//
//		JSONObject sessionUser = new JSONObject();
//		sessionUser.put("cosConnector", connector);
//
//		FileData cdata = new FileData();
//		cdata.setKeyId("00406f6f99a60400");
//		cdata.setUrl("https%3A%2F%2Fcos.lenovows.com%2Fv2%2Fobject%2F004028387b43ec00%3Alenote%2F00406f6f99a60400");
//
//		FileData.PublicLink publicLink = cdata.new PublicLink();
//		publicLink.setValidTime(10000);
//		cdata.setPublicLink(publicLink);
//
//		dataArray[0] = cdata;
//
//		FileData cdata2 = new FileData();
//		cdata2.setKeyId("0040694757470300");
//		cdata2.setUrl("https%3A%2F%2Fcos.lenovows.com%2Fv2%2Fobject%2F004028387b43ec00%3Alenote%2F0040694757470300");
//
//		FileData.PublicLink publicLink2 = cdata2.new PublicLink();
//		publicLink.setValidTime(10000);
//		cdata2.setPublicLink(publicLink2);
//
//		dataArray[1] = cdata2;
//
//		COSExecutionTask costask = new COSExecutionTask(
//				COSExecutionSignal.BATCH_CREATE_PUBLICLINKS, connector,
//				dataArray);
//		Future<Object> future = threadPoolTaskExecutor.submit(costask);
//		try {
//			Object returnObj = future.get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	@Test
//	@Ignore
//	public void batchPutObjectList() {
//		FileData[] dataArray = new FileData[2];
//
//		JSONObject sessionUser = new JSONObject();
//		sessionUser.put("cosConnector", connector);
//
//		FileData cdata = new FileData();
//		cdata.setKeyId("key3");
//		cdata.setFileCachePath("C:\\Users\\tangzj1\\Pictures\\1.jpg");
//		cdata.setContentType("image/jpeg");
//		cdata.setSize(561276L);
//
//		dataArray[0] = cdata;
//
//		FileData cdata2 = new FileData();
//		cdata2.setKeyId("key4");
//		cdata2.setFileCachePath("D:\\supernote\\return1.jpg");
//		File testFile = new File("D:\\supernote\\return1.jpg");
//		// cdata2.setTempFilePath("C:\\Users\\tangzj1\\Pictures\\2.jpg");
//		cdata2.setContentType("image/jpeg");
//		cdata2.setSize(testFile.length());
//
//		dataArray[1] = cdata2;
//
//		COSExecutionTask costask = new COSExecutionTask(
//				COSExecutionSignal.BATCH_PUT_OBJECTS, connector, dataArray);
//		Future<Object> future = threadPoolTaskExecutor.submit(costask);
//		try {
//			Object returnObj = future.get();
//			FileData cdata3 = new FileData();
//			cdata3.setKeyId("key3");
//			cdata3.setSize(-1l);
//			COSExecutionTask costask2 = new COSExecutionTask(
//					COSExecutionSignal.GET_OBJECT, connector, null, cdata3);
//			Future<Object> future2 = threadPoolTaskExecutor.submit(costask2);
//			FileData returnObj2 = (FileData) future2.get();
//			System.out.println("Object key:" + returnObj2.getKeyId());
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	@Test
//	@Ignore
//	public void batchGetObjectList() {
//		FileData[] dataArray = new FileData[2];
//
//		JSONObject sessionUser = new JSONObject();
//		sessionUser.put("cosConnector", connector);
//
//		FileData cdata = new FileData();
//		cdata.setKeyId("key1");
//		cdata.setFileCachePath("D:\\supernote\\attachment");
//		cdata.setSize(561276L);
//		dataArray[0] = cdata;
//
//		FileData cdata2 = new FileData();
//		cdata2.setKeyId("key2");
//		cdata2.setFileCachePath("D:\\supernote\\attachment");
//		cdata2.setSize(777835L);
//		dataArray[1] = cdata2;
//
//		COSExecutionTask costask = new COSExecutionTask(
//				COSExecutionSignal.BATCH_GET_OBJECTS, connector, dataArray);
//		Future<Object> future = threadPoolTaskExecutor.submit(costask);
//		try {
//			@SuppressWarnings("unused")
//			Object returnObj = future.get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
}
