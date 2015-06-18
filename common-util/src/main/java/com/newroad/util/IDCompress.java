package com.newroad.util;


/**
 * ID压缩
 */
public class IDCompress {
	
	private IDCompress(){
	}
	
	private static final String KEY = "lenovo_leNote";
	
	private static final String[] chars = new String[] { "a" , "b" , "c" , "d" , "e" , "f" , "g" , "h" ,
																	         "i" , "j" , "k" , "l" , "m" , "n" , "o" , "p" , "q" , "r" , "s" , "t" ,
																	         "u" , "v" , "w" , "x" , "y" , "z" , "0" , "1" , "2" , "3" , "4" , "5" ,
																	         "6" , "7" , "8" , "9" , "A" , "B" , "C" , "D" , "E" , "F" , "G" , "H" ,
																	         "I" , "J" , "K" , "L" , "M" , "N" , "O" , "P" , "Q" , "R" , "S" , "T" ,
																	         "U" , "V" , "W" , "X" , "Y" , "Z", "+" , "-" };
	
	/**
	 * 自增策略
	 */
	private static long increment = 0; 
	
	/**
	 * @deprecated 不能确保唯一性，请使用 {@link #compressID(String)}
	 */
	public synchronized static String getShortID(String id){
		String ids[] = compressID(id+(increment++));
		int index = ((int)(Math.random()*100))%4;
		return ids[index];
	}
	
	public synchronized static String[] compressID(String id) {
	  return compressID(id, 10);
	}
	
	public synchronized static String[] compressID(String id, int length) { 
       String hex = getMD5(KEY + id+(increment++)); ;		// 对传入网址进行 MD5 加密
 
       String[] resUrl = new String[4];
       for ( int i = 0; i < 4; i++) {
 
           // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
           String sTempSubString = hex.substring(i * 8, i * 8 + 8);
 
           // 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用 long ，则会越界
           long lHexLong = 0x3FFFFFFF & Long.parseLong (sTempSubString, 16);
           String outChars = "" ;
           for ( int j = 0; j < length; j++) {
              // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引 ,我加了2个为64=》 0x0000003f
              //long index = 0x0000003D & lHexLong;
        	   long index = 0x0000003F & lHexLong;
              // 把取得的字符相加
              outChars += chars[( int ) index];
              // 每次循环按位右移 2 位
              lHexLong = lHexLong >> 2;
           }
           // 把字符串存入对应索引的输出数组
           resUrl[i] = outChars;
       }
       return resUrl;      
	}
	    
	public synchronized static String getMD5(String target) {
		byte[] source = target.getBytes();
		String s = null;
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'}; 
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance( "MD5" );
			md.update( source );
			byte tmp[] = md.digest();          			// MD5 的计算结果是一个 128 位的长整数，
	                                                				// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2];   			// 每个字节用 16 进制表示的话，使用两个字符，
																	// 所以表示成 16 进制需要 32 个字符
			int k = 0;                                			// 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) {          		// 从第一个字节开始，对 MD5 的每一个字节
	    															// 转换成 16 进制字符的转换
				byte byte0 = tmp[i];                 		// 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];  	// 取字节中高 4 位的数字转换, 
	                                                							// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf];         // 取字节中低 4 位的数字转换
			} 
			s = new String(str);                        // 换后的结果转换为字符串
	   } catch( Exception e ) {
		   e.printStackTrace();
	   }
	   return s;
	 }

}
