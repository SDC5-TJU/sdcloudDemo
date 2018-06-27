package scs.util.repository;
  
 /**
  * 系统静态仓库类
  * 通过静态变量的形式为系统运行中需要用到的数据提供内存型存储
  * 包括一些系统参数，应用运行数据，控制标志等
  * @author yanan
  *
  */
public class Repository{ 
	private static Repository repository=null;
	private Repository(){}
	public synchronized static Repository getInstance() {
		if (repository == null) {
			repository = new Repository();
		}
		return repository;
	}  
	 
 
	

}
