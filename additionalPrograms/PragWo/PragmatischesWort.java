/*
 * Created on 15.06.2005 TODO To change the template for this generated
 * file go to Window - Preferences - Java - Code Style - Code Templates
 */

package additionalPrograms.PragWo;

/**
 * @author Natascha Stäbler natascha@mainiero.de<br>
 *         <br>
 *         Last modified on 04.11.2005<br>
 *         <br>
 *         Diese Klasse ist die Datenstruktur zur Speicherung der
 *         Analyseergebnisse für jedes Wort.
 */
public class PragmatischesWort {

	private String content = null;

	private int chapter = 0;

	private int start = 0;

	private int end = 0;

	private byte kind = 0;

	private int level = 0;

	private int path = 0;

	private String pathString = null;

	/**
	 * Der Konstrukor ist neben vielen Gettern die einzige Funktion dieser
	 * Klasse. Ihm werden alle zu speichernden Informationen übergeben.
	 * 
	 * @param content
	 *            das (Teil-)Wort
	 * @param chapter
	 *            das Kapitel
	 * @param start
	 *            der Beginn des Wortes
	 * @param end
	 *            das Ende des Wortes
	 * @param kind
	 *            die Wortart
	 * @param level
	 *            die Abstraktheit
	 * @param path
	 *            die ID des Knotens im Pfadbaum
	 * @param pathString
	 *            der komplette Pfad
	 */
	public PragmatischesWort(String content, int chapter, int start, int end,
			byte kind, int level, int path, String pathString) {
		this.content = content;
		this.chapter = chapter;
		this.start = start;
		this.end = end;
		this.kind = kind;
		this.level = level;
		this.path = path;
		this.pathString = pathString;
	}

	/**
	 * Gibt die Wortart zurück.
	 * 
	 * @return Returns the kind.
	 */
	public byte getKind() {
		return kind;
	}

	/**
	 * Gibt die Abstraktheit zurück.
	 * 
	 * @return Returns the level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gibt die Pfad-ID zurück.
	 * 
	 * @return Returns the path.
	 */
	public int getPath() {
		return path;
	}

	/**
	 * Gibt das Kapitel zurück.
	 * 
	 * @return Returns the chapter.
	 */
	public int getChapter() {
		return chapter;
	}

	/**
	 * Gibt den Wortstring zurück.
	 * 
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Gibt das Ende des Wortes zurück.
	 * 
	 * @return Returns the end.
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * Gibt den Anfang des Wortes zurück.
	 * 
	 * @return Returns the start.
	 */
	public int getStart() {
		return start;
	}

	/**
	 * Gibt den kompletten Pfad zurück.
	 * 
	 * @return Returns the pathString.
	 */
	public String getPathString() {
		return pathString;
	}
}
