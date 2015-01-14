package client.Fjorde;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;

public abstract class RotatedTile {
	public static BufferedImage getImage(String type, int orientation, int width, int height) throws IOException {
		if (type.length() != 6)
			return null;
		
		BufferedImage tile = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		char[] typeC = new char[6];
		type.getChars(0, type.length(), typeC, 0);
		
		//Rotation de la chaine
		char tmp1, tmp2;
		for (int i = 0; i < orientation; i++) {
			tmp2 = typeC[0];
			for (int j = 0; j < 6; j++) {
				tmp1 = typeC[(j+1)%6];
				typeC[(j+1)%6] = tmp2;
				tmp2 = tmp1;
			}
		}
		
		//Points de l'hexagone
		Point center = new Point(width/2, height/2);
		Point[] points = new Point[6];
		points[0] = new Point(width/2, 0);
		points[1] = new Point(width-1, height/4);
		points[2] = new Point(width-1, 3*height/4);
		points[3] = new Point(width/2, height);
		points[4] = new Point(0      , 3*height/4);
		points[5] = new Point(0      , height/4);
		
		//Dessin de l'hexagone avec des triangles
		Graphics g = tile.getGraphics();
		
		for ( int i = 0; i < 6; i++) {
			//Creation du triangle
			Polygon triangle = new Polygon();
			triangle.addPoint(center.x, center.y);
			triangle.addPoint(points[i].x, points[i].y);
			triangle.addPoint(points[(i+1)%6].x, points[(i+1)%6].y);
			
			//Couleur du triangle
			Color c;
			switch (typeC[i]) {
				case 'E':
					c = new Color(0x146b7d);
					break;
				case 'T':
					c = new Color(0x61a533);
					break;
				case 'M':
					c = new Color(0x79695d);
					break;
				case 'P':
					c = new Color(0xAA65c60a, true);
					break;
				default:
					c = Color.BLACK;
			}
			
			//Dessin du triangle sur le Graphics
			g.setColor(c);
			g.drawPolygon(triangle);
			g.fillPolygon(triangle);
		}
		
		//Dessin du contour de l'hexagone
		g.setColor(Color.BLACK);
		Polygon hexagon = new Polygon();
		for (Point p : points)
			hexagon.addPoint( p.x, p.y);
		g.drawPolygon(hexagon);
		
		//Dessin du centre
		Color centerColor;
		if ( type.contains("T"))
			centerColor = new Color(0x61a533);
		else if ( type.contains("E"))
			centerColor = new Color(0x146b7d);
		else
			centerColor = new Color(0x79695d);
		g.setColor(centerColor);
		g.fillOval(center.x-width/6, center.y-height/6, width/3, height/3);
		
		//Dessin sur le BufferedImage	
		g.drawImage(tile, 0, 0, null);
		
		return tile;
	}
}
