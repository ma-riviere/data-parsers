package com.geo.aion.geoEngine;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import com.geo.aion.geoEngine.math.Matrix3f;
import com.geo.aion.geoEngine.math.Vector3f;
import com.geo.aion.geoEngine.models.GeoMap;
import com.geo.aion.geoEngine.scene.Geometry;
import com.geo.aion.geoEngine.scene.Mesh;
import com.geo.aion.geoEngine.scene.Node;
import com.geo.aion.geoEngine.scene.Spatial;
import com.geo.aion.geoEngine.scene.VertexBuffer;

/**
 * @author Mr. Poke
 */
public class GeoWorldLoader {

	private static String GEO_DIR = "../../Data/geo/"; //TODO: Move to property

	private static boolean DEBUG = false;

	public static void setDebugMod(boolean debug) {
		DEBUG = debug;
	}

	public static Map<String, Spatial> loadMeshs(String fileName) throws IOException {
		Map<String, Spatial> geoms = new HashMap<String, Spatial>();
		File geoFile = new File(fileName);
		FileChannel roChannel = null;
		MappedByteBuffer geo = null;
		roChannel = new RandomAccessFile(geoFile, "r").getChannel();
		int size = (int) roChannel.size();
		geo = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, size).load();
		geo.order(ByteOrder.LITTLE_ENDIAN);
		while (geo.hasRemaining()) {
			short namelenght = geo.getShort();
			byte[] nameByte = new byte[namelenght];
			geo.get(nameByte);
			String name = new String(nameByte);
			int modelCount = geo.getShort();
			Node node = new Node(DEBUG ? name : null);
			for (int c = 0; c < modelCount; c++) {
				Mesh m = new Mesh();
				int vectorCount = geo.getShort() * 3;
				FloatBuffer vertices = FloatBuffer.allocate(vectorCount);
				for (int x = 0; x < vectorCount; x++) {
					vertices.put(geo.getFloat());
				}
				int tringle = geo.getInt();
				ShortBuffer indexes = ShortBuffer.allocate(tringle);
				for (int x = 0; x < tringle; x++) {
					indexes.put(geo.getShort());
				}
				m.setBuffer(VertexBuffer.Type.Position, 3, vertices);
				m.setBuffer(VertexBuffer.Type.Index, 3, indexes);
				m.createCollisionData();
				Geometry geom = new Geometry(null, m);
				if (modelCount == 1)
					geoms.put(name, geom);
				node.attachChild(geom);
			}
			if (!node.getChildren().isEmpty()) {
				geoms.put(name, node);
			}
		}
		return geoms;
	}

	public static boolean loadWorld(int worldId, Map<String, Spatial> models, GeoMap map) throws IOException {
		File geoFile = new File(GEO_DIR + worldId + ".geo");
		FileChannel roChannel = null;
		MappedByteBuffer geo = null;
		roChannel = new RandomAccessFile(geoFile, "r").getChannel();
		geo = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) roChannel.size()).load();
		geo.order(ByteOrder.LITTLE_ENDIAN);
		if (geo.get() == 0)
			map.setTerrainData(new short[] { geo.getShort() });
		else {
			int size = geo.getInt();
			short[] terrainData = new short[size];
			for (int i = 0; i < size; i++)
				terrainData[i] = geo.getShort();
			map.setTerrainData(terrainData);
		}
		while (geo.hasRemaining()) {
			int nameLenght = geo.getShort();
			byte[] nameByte = new byte[nameLenght];
			geo.get(nameByte);
			String name = new String(nameByte);
			Vector3f loc = new Vector3f(geo.getFloat(), geo.getFloat(), geo.getFloat());
			float[] matrix = new float[9];
			for (int i = 0; i < 9; i++)
				matrix[i] = geo.getFloat();
			float scale = geo.getFloat();
			Matrix3f matrix3f = new Matrix3f();
			matrix3f.set(matrix);
			Spatial node = models.get(name.toLowerCase());
			if (node != null) {
				Spatial nodeClone = null;
				try {
					nodeClone = node.clone();
				}
				catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				nodeClone.setTransform(matrix3f, loc, scale);
				nodeClone.updateModelBound();
				map.attachChild(nodeClone);
			}
		}
		map.updateModelBound();
		return true;
	}
}
