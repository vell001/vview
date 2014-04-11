package vell.bibi.vview.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * @author VellBibi
 *
 */
public class MakeCertPic {
	// 验证码图片中可以出现的字符集，可以根据需要修改
	private char mapTable[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9' };

	/**
	 * 功能：生成彩色验证码图片 参数wedth为生成图片的宽度，参数height为生成图片的高度，参数os为页面的输出流
	 * @param width
	 * @param height
	 * @param n 生成验证码的字符数
	 * @param os
	 * @return
	 */
	public String getCertPic(int width, int height, int n, OutputStream os) {
		if (width<=0 || height<=0 || n<=0){
			width = 60;
			height = 20;
			n = 4;
		}
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();
		// 设定背景颜色
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		// 画边框
		g.setColor(Color.blue);
		g.drawRect(0, 0, width - 1, height - 1);
		// 随机产生的验证码
		String strEnsure = "";
		// 4代表4为验证码，如果要产生更多位的验证码，则加大数值
		for (int i = 0; i < n; ++i) {
			strEnsure += mapTable[(int) (mapTable.length * Math.random())];
		}
		// 将认证码显示到图像中，如果要生成更多位的验证码，增加drawString语句
		g.setColor(Color.blue);
		g.setFont(new Font("Atlantic Inline", Font.PLAIN, (int)((height*0.8)+ (height*0.2)*Math.random())));
		
		String str = null;
		for(int i=0; i<n; i++){
			str = strEnsure.substring(i, i+1);
			g.drawString(str, (int)(((width-16)/n)*(i)+12*Math.random()+8),(int)((height*0.8)+ (height*0.2)*Math.random()));
		}
		// 随机产生15个干扰点
		Random rand = new Random();
		for (int i = 0; i < 100; i++) {
			int x = rand.nextInt(width);
			int y = rand.nextInt(height);
			g.drawOval(x, y, 1, 1);
		}
		// 释放图形上下文
		g.dispose();
		try {
			// 输出图形到页面
			ImageIO.write(image, "JPEG", os);
		} catch (IOException e) {
			return "";
		}
		return strEnsure;
	}
}
