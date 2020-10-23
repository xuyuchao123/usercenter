package com.xyc.userc.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Pattern;

import jxl.Sheet;
import jxl.Workbook;

public class Message3 
{
	public static void main(String[] args)
	{
		File file = new File("src/message3.xls");

		try {
			// ����EXCEL
			Sheet sheet = Workbook.getWorkbook(new FileInputStream(file)).getSheet(0);
			String telNum = null;
//			String regex = "^1[0-9]{10}$";
//			Pattern p = Pattern.compile(regex);
			StringBuilder sb = new StringBuilder();
			sb.append("insert into ieps.UUP_SUPPLIER_MESSAGE(USER_ID,CONTENT,MOBILE_PHONE) ");
			sb.append("values(ieps.SEQ_UUP_SUPPLIER_MESSAGE.nextval,");
			String str = "'请重新完善 身份证地址或通信地址 后再次提交，截止日期2020年10月24日下班前。','";
			sb.append(new String(str.getBytes("utf-8"),"utf-8"));
			String start = sb.toString();
			StringBuilder sb2 = new StringBuilder();
			ArrayList<String> set = new ArrayList<String>();
			for(int j=0 ; j<sheet.getRows(); j++)
			{
//				System.out.println(sheet.getCell(2,j).getContents());
				telNum = sheet.getCell(0,j).getContents();
				set.add(telNum);
			}
			
//			for(String s0 : set)
//			{
//				System.out.println(s0);
//			}
//			System.out.println("*********************************");
			for(String s : set)
			{
				sb2.append(start);
				sb2.append(s);
				sb2.append("');");
//				System.out.println(sb2.toString());
				System.out.println(new String(sb2.toString().getBytes("UTF-8"),"UTF-8"));
				sb2.delete(0, sb2.length());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
