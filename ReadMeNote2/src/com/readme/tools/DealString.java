package com.readme.tools;


public class DealString {
	
		/** 
		* @param args 
		* @author 海文 
		* 有待完善
		*/ 
		public static String cutString(String str) { 
		
		String[] array= new String[10];
		String temp = str;//定义一个 字符串变量，把str赋给他，保持str字符串不变 
		String summry = "";
		
		/* 
		* 第一个for循环用于对字符串进行分割 
		* 寻找空格 的 位置 ，然后进行截取，当寻找到最后 找不到空格的 时候，indexOf()方法会返回-1这个值，表示找不到了。 
		*/ 
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < array.length; i++) { 
			int index1 = temp.indexOf("http");//寻找空格的位置 
			int index2 = temp.indexOf("\">");
			if (index1 == -1) { 
				array[i] = temp;//找不到空格的时候表示就剩下最后一个字符串了 ，不需要分割，直接赋值给数组，然后break跳出循环。 
				break; 
			} 
			array[i] = temp.substring(index1, index2); 
			temp = temp.substring(index2 + 2); 
			
			 summry = result.append(summry).append(array[i]).toString();
			System.out.println(summry);
			} 
		return summry;
	} 
		
}
