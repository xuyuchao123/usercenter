package com.xyc.userc.util;

import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.regex.Pattern;

public class Message 
{
	public static void main(String[] args)
	{
		File file = new File("src/msg11.xls");

		try {
			// ����EXCEL
			Sheet sheet = Workbook.getWorkbook(new FileInputStream(file)).getSheet(0);
			String cell7 = null;
			String cell8 = null;
			String cell9 = null;
			String regex = "^1[0-9]{10}$";
			Pattern p = Pattern.compile(regex);
			StringBuilder sb = new StringBuilder();
			sb.append("insert into ieps.UUP_SUPPLIER_MESSAGE(USER_ID,CONTENT,MOBILE_PHONE) ");
			sb.append("values(ieps.SEQ_UUP_SUPPLIER_MESSAGE.nextval,");
			String str = "'通知：退休职工春节物资开始发啦！即日起至2月9日每天上午8点到下午5点在沙钢体育馆发放。退休职工须提供身份证和手机号码。如代领，须提供代领人及退休职工的身份证和手机号码。领取时须戴口罩。    （沙钢工会）','";
			sb.append(new String(str.getBytes("utf-8"),"utf-8"));
			String start = sb.toString();
			StringBuilder sb2 = new StringBuilder();
			HashSet<String> set = new HashSet();
			for(int j=2 ; j<sheet.getRows(); j++)
			{
//				System.out.println(sheet.getCell(2,j).getContents());
				cell7 = sheet.getCell(7,j).getContents();
				cell8 = sheet.getCell(8,j).getContents();
				cell9 = sheet.getCell(9,j).getContents();
				String telNum = !"".equals(cell9) ? cell9 : (!"".equals(cell8) ? cell8 : cell7);
				if(!"".equals(telNum) && telNum.length() >= 11)
				{
//					if(telNum.indexOf("/") == -1)
//					{
//						System.out.println(telNum);
//					}
//					else
//					{
//						String[] strs = telNum.split("/");
//						for(String s : strs)
//						{
//							if(s.startsWith("1"))
//							{
//								telNum = s;
//								break;
//							}
//						}
//						System.out.println(telNum);
//					}
					for(int k = 0; k <= telNum.length()-11; k++)
					{
						if(p.matcher(telNum.substring(k,k+11)).matches())
						{
//							System.out.println(telNum.substring(k,k+11));
							set.add(telNum.substring(k,k+11));
							break;
						}
					}
				}
				
				cell7 = null;
				cell8 = null;
				cell9 = null;
			}
			
			for(String s0 : set)
			{
				System.out.println(s0);
			}
			System.out.println("*********************************");
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
