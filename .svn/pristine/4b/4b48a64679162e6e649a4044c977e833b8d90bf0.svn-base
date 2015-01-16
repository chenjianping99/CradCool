package com.jiubang.goscreenlock.theme.cjpcardcool.music;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

/**
 * 
 * @author liangdaijian
 * 
 */
public class MusicControlCenter extends BroadcastReceiver {
	private static final String MEDIAPLAYER_BY_DEFAULT = "com.android.music";
	private static final String MEDIAPLAYER_BY_KUGOO = "com.kugou.android.music";
	private static final String MEDIAPLAYER_BY_TTPOD = "com.sds.android.ttpod";
	private static final String MEDIAPLAYER_BY_POWERAMP = "com.maxmpz.audioplayer";
	private static final String MEDIAPLAYER_BY_KKBOX = "com.skysoft.kkbox.android";

	private static final String MUSIC_SONG_NAME = "track";

	private static final int MUSIC_START = 0;
	private static final int MUSIC_PAUSE = 1;
	private static final int MUSIC_NEXT = 2;
	private static final int MUSIC_PRE = 3;

	private static final int MUSIC_COMMAND_TOGGLEPAUSE = 85;
	private static final int MUSIC_COMMAND_PAUSE = 85;
	private static final int MUSIC_COMMAND_NEXT = 87;
	private static final int MUSIC_COMMAND_PRE = 88;

	private static final long EVENT_DONW_TIME = 65535;
	private static final long EVENT_TIME = 67535;

	private static final int SMACHINE_ID = 248;
	private static final String MEDIA_BUTTON_ACTION = "android.intent.action.MEDIA_BUTTON";
	private static final String MEDIA_SERVICE_CMD = "com.android.music.musicservicecommand";
	private static final String MEDIA_BUTTON_ACTION_COMMAND = "command";

	private static final String MUSIC_LABLE = "musicplay";

	public static final int sMEDIA_BUTTION_CONTROL_TYPE = 1;
	public static final int sMEDIA_BROCAST_CONTROL_TYPE = 2;

	private MusicInfoBean mMusicInfoBean = null;
	private ArrayList<MusicInfoListener> mListenerArray = null;

	public MusicControlCenter() {
		mListenerArray = new ArrayList<MusicInfoListener>();
		mMusicInfoBean = new MusicInfoBean();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (null == action) {
			return;
		}

		mMusicInfoBean = getMusicBean(intent);

		if (null == mMusicInfoBean) {
//			return;
		}

		for (int i = 0; i < mListenerArray.size(); i++) {
			mListenerArray.get(i).notifyMusicInfoChanged(mMusicInfoBean);
		}
	}

	public void setMusicInfoUpdateListener(MusicInfoListener listener) {
		mListenerArray.add(listener);
	}

	public MusicInfoBean getCurMusicInfo() {
		return mMusicInfoBean;
	}

	public void resignReceiver(Context context) {
		IntentFilter intentFileter = new IntentFilter();

		// 系统原生播放器
		intentFileter.addAction("com.android.music.metachanged");
		intentFileter.addAction("com.android.music.queuechanged");
		intentFileter.addAction("com.android.music.playbackcomplete");
		intentFileter.addAction("com.android.music.playstatechanged");

		// 酷狗播放器
		intentFileter.addAction("com.kugou.android.music.metachanged");
		intentFileter.addAction("com.kugou.android.music.queuechanged");
		intentFileter.addAction("com.kugou.android.music.playbackcomplete");
		intentFileter.addAction("com.kugou.android.music.playstatechanged");

		intentFileter.addAction("musicPlayer.service.updateMediaInfo");
		intentFileter.addAction("musicPlayer.service.updatePlayInfo");
		intentFileter.addAction("musicPlayer.service.startUpdateStatus");

		// 天天动听
		intentFileter.addAction("com.sds.android.ttpod.playstatechanged");
		intentFileter.addAction("com.sds.android.ttpod.metachanged");

		// powerAmp
		intentFileter.addAction("com.maxmpz.audioplayer.TRACK_CHANGED");
		intentFileter.addAction("com.maxmpz.audioplayer.AA_CHANGED");
		intentFileter.addAction("com.maxmpz.audioplayer.STATUS_CHANGED");
		intentFileter.addAction("com.maxmpz.audioplayer.PLAYING_MODE_CHANGED");

		// KKBox
		intentFileter.addAction("com.skysoft.kkbox.android.ACTION_PLAYER_WIDGET_UPDATE");

		context.registerReceiver(this, intentFileter);

	}

	/**
	 * 反注册接收者，防止内存泄露
	 * 
	 * @param context
	 */
	public void unregisterReceiver(Context context) {
		context.unregisterReceiver(this);
	}

	/**
	 * 播放音乐
	 * 
	 * @param context
	 */
	public static void play(Context context) {
		sendMusicCotrolMsg(context, MUSIC_START);
	}

	/**
	 * 暂停播放
	 * 
	 * @param context
	 */
	public static void pause(Context context) {
		sendMusicCotrolMsg(context, MUSIC_PAUSE);
	}

	/**
	 * 下一首
	 * 
	 * @param context
	 */
	public static void next(Context context) {
		sendMusicCotrolMsg(context, MUSIC_NEXT);
	}

	/**
	 * 上一首
	 * 
	 * @param context
	 */
	public static void previous(Context context) {
		sendMusicCotrolMsg(context, MUSIC_PRE);
	}

	/**
	 * 返回音乐是否正在播放
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMusicPlaying(Context context) {
		AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		return mAudioManager.isMusicActive();
	}

	/**
	 * 获取音乐信息
	 * 
	 * @param intent
	 * @return
	 */
	private MusicInfoBean getMusicBean(Intent intent) {
		String action = intent.getAction();
		String songName = null;
		String songPlayer = null;
		// String songALbum = null;
		try {
			if (action.contains(MEDIAPLAYER_BY_DEFAULT)) {
				songName = intent.getStringExtra(MUSIC_SONG_NAME);
				songPlayer = intent.getStringExtra("artist");
			} else if (action.contains(MEDIAPLAYER_BY_KKBOX)) {
				songName = intent.getStringExtra("com.skysoft.kkbox.android.EXTRA_SONG_NAME");
				songPlayer = intent.getStringExtra("com.skysoft.kkbox.android.EXTRA_ARTIST_NAME");
			} else if (action.contains(MEDIAPLAYER_BY_KUGOO)) {
				songName = intent.getStringExtra(MUSIC_SONG_NAME);
				songPlayer = intent.getStringExtra("artist");
			} else if (action.contains(MEDIAPLAYER_BY_POWERAMP)) {
				Bundle localBundle = (Bundle) intent.getParcelableExtra(MUSIC_SONG_NAME);
				songName = localBundle.getString("title");
				songPlayer = localBundle.getString("artist");
			} else if (action.contains(MEDIAPLAYER_BY_TTPOD)) {
				Bundle localBundle = (Bundle) intent.getParcelableExtra("com.sds.android.ttpod.bundle.mediaitem");
				if (null != localBundle) {
					songName = localBundle.getString("title");
					songPlayer = localBundle.getString("artist");
				}
			}
		} catch (Exception e) {
			if (null == songName) {
				return null;
			}
		}

		if (null == songName) {
			return null;
		}

		MusicInfoBean bean = new MusicInfoBean();
		bean.setTrack(songName);
		bean.setArtist(songPlayer);

		return bean;
	}

	private static void sendMusicCotrolMsg(Context context, int type) {
		String command = null;
		int commandNum = 85;
		switch (type) {
			case MUSIC_START : {
				command = "togglepause";
				commandNum = MUSIC_COMMAND_TOGGLEPAUSE;
			}
				break;

			case MUSIC_PAUSE : {
				command = "pause";
				commandNum = MUSIC_COMMAND_PAUSE;
			}
				break;

			case MUSIC_NEXT : {
				command = "next";
				commandNum = MUSIC_COMMAND_NEXT;
			}
				break;

			case MUSIC_PRE : {
				command = "previous";
				commandNum = MUSIC_COMMAND_PRE;
			}
				break;

			default :
				break;
		}

		if (null != command) {

			Log.i(MUSIC_LABLE, command);
			if (1 % 2 == sMEDIA_BUTTION_CONTROL_TYPE) {
				Intent localIntent1 = new Intent(MEDIA_BUTTON_ACTION, null);

				localIntent1.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(EVENT_DONW_TIME, EVENT_TIME, KeyEvent.ACTION_DOWN, commandNum, 0, 0, SMACHINE_ID, 226, 8));
				localIntent1.putExtra(MEDIA_BUTTON_ACTION_COMMAND, command);
				context.sendOrderedBroadcast(localIntent1, null);

				localIntent1.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(EVENT_DONW_TIME, EVENT_TIME, KeyEvent.ACTION_UP, commandNum, 0, 0, SMACHINE_ID, 0, 8));
				localIntent1.putExtra(MEDIA_BUTTON_ACTION_COMMAND, command);
				context.sendOrderedBroadcast(localIntent1, null);
			} else {
				Intent intent = new Intent(MEDIA_SERVICE_CMD);
				intent.putExtra(MEDIA_BUTTON_ACTION_COMMAND, command);
				context.sendBroadcast(intent);
			}
		}
	}

}
