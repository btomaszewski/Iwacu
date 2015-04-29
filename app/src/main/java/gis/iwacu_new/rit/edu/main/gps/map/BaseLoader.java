package gis.iwacu_new.rit.edu.main.gps.map;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 *
 */
public abstract class BaseLoader extends Thread  {

	public static final int CONNECTION_TIMEOUT = 10000;

	private RawTile[] tiles;

    /**
     *
     */
	private volatile boolean stop = false;

	public BaseLoader(RawTile tile) {
		super("HTTP");
		this.tiles = new RawTile[1];
		this.tiles[0] = tile;
	}

	public BaseLoader(RawTile[] tiles) {
		super("HTTP");
		this.tiles = tiles;
	}

    /**
     * Stops the loader.
     */
	public void stopLoader() {
		stop = true;
	}

	public void run() {
        // Note: Before changing this code please read about threading and inter-thread
        //       communication. I hope you really know what you're doing. XD
		for (RawTile tile : tiles) {
			if (stop) {
				return;
			}
			if (checkTile(tile)) {
				try {
					byte[] data = load(tile);
					handle(tile, data, 0);
				} catch (Exception e) {
					e.printStackTrace();
					handle(tile, null, 0);
				}
			} else {
				handle(tile, null, 1);
			}
		}

	}

	protected boolean checkTile(RawTile tile) {
		return true;
	}

	private byte[] load(RawTile tile) {
		HttpURLConnection connection = null;
		try {
			String tilePath = getStrategy().getURL(tile.x, tile.y, tile.z, 0);
			URL u = new URL(tilePath);
			connection = (HttpURLConnection) u.openConnection();
			connection.setRequestMethod("GET");
			connection.setReadTimeout(BaseLoader.CONNECTION_TIMEOUT);
			connection.setConnectTimeout(BaseLoader.CONNECTION_TIMEOUT);
			connection.connect();
			int responseCode = connection.getResponseCode();
			if (responseCode!= HttpURLConnection.HTTP_OK) {
//				Log.message("loading failed", tilePath+ " "+connection.getResponseMessage()+" " + connection.getResponseCode());
				return null;
			}
			int contentLength = connection.getContentLength();
			InputStream raw = connection.getInputStream();
			InputStream in = new BufferedInputStream(raw, 4096);
			byte[] data = new byte[contentLength];
			int bytesRead = 0;
			int offset = 0;
			while (offset < contentLength) {
				bytesRead = in.read(data, offset, data.length - offset);
				if (bytesRead == -1)
					break;
				offset += bytesRead;
			}
			in.close();
			if (offset != contentLength) {
				Log.message("loading failed","invalid offset "+ connection.getResponseMessage()+" " + connection.getResponseCode());
				return null;
			}
			return data;
		}catch(SocketTimeoutException e){
			e.printStackTrace();
			System.out.println("timeout");
		}
		catch (Exception e) {
			Log.message("loading failed: exception", e.getMessage());
			e.printStackTrace();
		} finally{
			connection.disconnect();
		}
		Log.message("loading failed","return null");
		return null;

	}

	protected abstract void handle(RawTile tile, byte[] data, int meta);

	protected abstract MapStrategy getStrategy();

}
