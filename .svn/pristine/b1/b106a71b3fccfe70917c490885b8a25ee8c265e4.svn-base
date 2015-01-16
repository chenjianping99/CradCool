package com.jiubang.goscreenlock.theme.cjpcardcool.view.bg;

import java.io.File;

/**
 * 
 * <br>类描述: 按文件名排序
 * <br>功能详细描述:
 * 
 * @author  xuqian
 * @date  [2012-9-15]
 */
public class FileSort implements Comparable
{
	private File	mFile;

	public FileSort(File file)
	{
		mFile = file;
	}

	@Override
	public int compareTo(Object obj)
	{
		FileSort fileSort = (FileSort) obj;
		if (this.mFile.getName().compareTo(fileSort.mFile.getName()) > 0)
		{
			return 1;
		}
		else if (this.mFile.getName().compareTo(fileSort.mFile.getName()) < 0)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}

	public File getFile()
	{
		return mFile;
	}


	
}
