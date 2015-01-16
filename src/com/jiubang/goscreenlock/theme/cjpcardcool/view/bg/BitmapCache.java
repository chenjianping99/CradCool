package com.jiubang.goscreenlock.theme.cjpcardcool.view.bg;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

import android.graphics.Bitmap;

/**
 * 
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  xuqian
 * @date  [2012-9-13]
 */
public class BitmapCache
{
	static private BitmapCache				sCache;
	/** 用于Chche内容的存储 */
	private Hashtable<Integer, MySoftRef>	mHashRefs;
	/** 垃圾Reference的队列（所引用的对象已经被回收，则将该引用存入队列中） */
	private ReferenceQueue<Bitmap>			mQ;

	/**
	 * 继承SoftReference，使得每一个实例都具有可识别的标识。
	  */
	private class MySoftRef extends SoftReference<Bitmap>
	{
		private Integer	mKey	= 0;

		public MySoftRef(Bitmap bmp, ReferenceQueue<Bitmap> q, int key)
		{
			super(bmp, q);
			mKey = key;
		}
	}

	private BitmapCache()
	{
		mHashRefs = new Hashtable<Integer, MySoftRef>();
		mQ = new ReferenceQueue<Bitmap>();
	}

	/**
	 * 取得缓存器实例
	  */
	public static BitmapCache getInstance()
	{
		if (sCache == null)
		{
			sCache = new BitmapCache();
		}
		return sCache;
	}

	/**
	 * 以软引用的方式对一个Bitmap对象的实例进行引用并保存该引用
	  */
	public void addCacheBitmap(Bitmap bmp, Integer position)
	{
		cleanCache(); // 清除垃圾引用
		MySoftRef ref = new MySoftRef(bmp, mQ, position);
		mHashRefs.put(position, ref);
	}

	/**
	 * 依据所指定的drawable下的图片资源ID号（可以根据自己的需要从网络或本地path下获取），重新获取相应Bitmap对象的实例
	 */
	public Bitmap getBitmap(int position)
	{
		Bitmap bmp = null;
		// 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得。
		if (mHashRefs.containsKey(position))
		{
			MySoftRef ref = (MySoftRef) mHashRefs.get(position);
			bmp = (Bitmap) ref.get();
		}
		return bmp;
	}

	public void updateCache(int position, int customSize)
	{
		int lastKey = -1;
		for (int i = position; i < position + customSize; i++)
		{
			if (mHashRefs.containsKey(i + 1))
			{
				mHashRefs.put(i, mHashRefs.get(i + 1));
				lastKey = i + 1;
			}
		}
		if (lastKey != -1 && mHashRefs.containsKey(lastKey))
		{
			mHashRefs.remove(lastKey);
		}
	}

	private void cleanCache()
	{
		MySoftRef ref = null;
		while ((ref = (MySoftRef) mQ.poll()) != null)
		{
			mHashRefs.remove(ref.mKey);
		}
	}

	/**
	 * 清除Cache内的全部内容
	 */
	public void clearCache()
	{
		cleanCache();
		mHashRefs.clear();
		System.gc();
		System.runFinalization();
	}
}