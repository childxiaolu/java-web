package edu.hzu.javaweb.labs.se1414080902119;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/1414080902119")
public class Se1414080902119servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//初始化基本数据
	private static List<BookType> types;
	static{
		types=new ArrayList<BookType>();
		types.add(new BookType("数学", false));
		types.add(new BookType("文学",false));
		types.add(new BookType("天文学",true));
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		String operate=request.getParameter("operate");
		System.out.println(request.getRequestURI());
		if(operate==null)operate="";
		if("show".equals(operate))doshow(response,request);
		else if("add".equals(operate))addTypes(response,request);
		else if("delete".equals(operate))deleteTypes(response,request);
		else showList(response);
	}
	
	private void doshow(HttpServletResponse response, HttpServletRequest request) throws IOException, ServletException {
		request.setAttribute("list", types);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/1414080902119/client.jsp");
		dispatcher.forward(request, response);
	}
	private void deleteTypes(HttpServletResponse response, HttpServletRequest request) throws IOException {
		// TODO Auto-generated method stub
		String name=request.getParameter("name");
		String str="";
		System.out.println(name);
		String[]arr=name.split(",");
		for(int j=0;j<arr.length;j++)
		{
			for(int i=0;i<types.size();i++){
				BookType type=types.get(i);
				String typeName=type.getName();
				if(typeName.equals(arr[j]))
				{
					if(type.getIsHasBook()==false)
					types.remove(type);
					else str+=type.getName()+",";
					break;
				}
			} 
		}
		if(str.length()>1){
			str=str.substring(0, str.length()-1);
		}
		PrintWriter writer = response.getWriter();
		if(str.length()==0){
			writer.write("{\"msg\":\"success\"}");
		}
		else writer.write("{\"msg\":\""+str+" 类别下有图书，不可删除"+"\"}");
		
		writer.close();
	}

	public void showList(HttpServletResponse response) throws IOException
	{
		PrintWriter writer = response.getWriter();
		writer.write(reJSON(types));
		writer.close();
	}
	
	public void addTypes(HttpServletResponse response,HttpServletRequest request)throws IOException
	{
		String name=request.getParameter("name");
		PrintWriter writer = response.getWriter();
		if(name==null||name.equals(""))
		{
		writer.write("{\"msg\":\"请输入图书类别\"}");
		writer.close();
		return ;
		}
		for(int i=0;i<types.size();i++){
			if(types.get(i).getName().equals(name))
			{
				writer.write("{\"msg\":\"该图书类别存在\"}");
				writer.close();
				return ;
			}
		}
		BookType type=new BookType();
		type.setName(name);
		type.setIsHasBook(false);
		types.add(type);
		writer.write("{\"msg\":\"success\"}");
		writer.close();
	}
	
	/**
	 * 返回JSON数据
	 * @param list   图书类别集合
	 * @return 图书类别的JSON数据
	 */
	public String reJSON(List<BookType> list)
	{
		StringBuffer str=new StringBuffer("{");
		Iterator<BookType> iterator = list.iterator();
		while(iterator.hasNext())
		{
			BookType bookType = iterator.next();
			str.append("\""+bookType.getName()+"\":\""+bookType.getIsHasBook()+"\"");
			if(iterator.hasNext())str.append(",");
		}
		str.append("}");
		return str.toString();
	}
}
