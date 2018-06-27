package scs.controller;
 
import java.util.Random; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import scs.pojo.PQOSBean; 
   
/**
 * 监控模块控制类
 * 负责页面请求的处理和转发
 * @author  
 * 
 */
@Controller
public class MonitorController {
	private Random rand=new Random();
	 
	/**
	 * 进入监控页面的请求
	 * @param request
	 * @param response
	 * @param model
	 * @return 监控页面
	 */
	@RequestMapping("/monitor.do")
	public String monitorLLC(HttpServletRequest request,HttpServletResponse response,Model model){
		 
		/**
		 * 在刚进入页面时,展示图需要60个初始化点绘制窗口,所以可以生成60个随机数点 <time,value>
		 * time这里取了当前时间的毫秒形式,value为随机值
		 */
		
		//绘制末级缓存缺失率的曲线
		StringBuilder strName=new StringBuilder();
		StringBuilder strData=new StringBuilder();
		StringBuilder HSeries=new StringBuilder();
		strName.append("{name:'llc',type:'area',");//type:'area',
		strData.append("data:["); 
		for(int i=0;i<59;i++){
			strData.append("[").append(System.currentTimeMillis()).append(",").append(rand.nextInt(100)).append("],");
		}
		strData.append("[").append(System.currentTimeMillis()).append(",").append(rand.nextInt(100)).append("]]");
		HSeries.append(strName).append(strData).append(",marker: {enabled: false}}");
		
		model.addAttribute("llc",HSeries.toString());//封装字符串发送到前端页面
		
		//绘制本地内存带宽MBL读写次数的曲线
		strName.setLength(0);
		strData.setLength(0);
		HSeries.setLength(0);
		strName.append("{name:'mbl',color:Highcharts.getOptions().colors[0],type:'line',");//type:'line'代表曲线; 'area'代表局域图
		strData.append("data:[");
		for(int i=0;i<59;i++){
			strData.append("[").append(System.currentTimeMillis()).append(",").append(rand.nextInt(100)).append("],");
		}
		strData.append("[").append(System.currentTimeMillis()).append(",").append(rand.nextInt(100)).append("]]");
		HSeries.append(strName).append(strData).append(",marker: {enabled: false}},");
		
		//绘制远程内存带宽MBR读写次数的曲线
		strName.setLength(0);
		strData.setLength(0); 
		strName.append("{name:'mbr',color:Highcharts.getOptions().colors[1],type:'line',");//type:'line'代表曲线; 'area'代表局域图
		strData.append("data:[");
		for(int i=0;i<59;i++){
			strData.append("[").append(System.currentTimeMillis()).append(",").append(rand.nextInt(100)).append("],");
		}
		strData.append("[").append(System.currentTimeMillis()).append(",").append(rand.nextInt(100)).append("]]");
		HSeries.append(strName).append(strData).append(",marker: {enabled: false}}");//拼接MBL和MBR的曲线
		
		model.addAttribute("mbm",HSeries.toString());//把拼接的MBL和MBR的曲线字符串封装,发送到前端页面monitor.jsp
		 
		return "monitor";
	}
	/**
	 * 前端页面每次请求新的数据 ajax形式请求
	 * @param response
	 * @return 封装好的新的数据 json字符串格式返回
	 */
	@RequestMapping(value="/getPqos.do")
	@ResponseBody
	public String getPqos(HttpServletResponse response) { 
		PQOSBean bean = new PQOSBean();
		bean.setTime(System.currentTimeMillis());//设置当前时间戳,因为前端主要靠时间戳判断是否数据有更新,如果时间戳重复则不添加新的点到图中
		bean.setMbl(rand.nextInt(50));//这里用了随机值做演示 设置MBL的值
		bean.setMbr(rand.nextInt(100));//这里用了随机值做演示 设置MBR的值
		bean.setCachemiss(rand.nextInt(100));//这里用了随机值做演示 设置cacheMiss的值
		
		return JSONArray.fromObject(bean).toString();//封装成json格式 返回
		
	}
}
