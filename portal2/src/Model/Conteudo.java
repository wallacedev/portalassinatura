package Model;

public class Conteudo {
	private int index;
	private String file;
	private String filename;
	private String filetype;
	private String politicatipo;
	private String politicasubtipo;
	private String base64;
	private String hash;
	
	public Conteudo() {
		
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getPoliticatipo() {
		return politicatipo;
	}

	public void setPoliticatipo(String politicatipo) {
		this.politicatipo = politicatipo;
	}

	public String getPoliticasubtipo() {
		return politicasubtipo;
	}

	public void setPoliticasubtipo(String politicasubtipo) {
		this.politicasubtipo = politicasubtipo;
	}
	
	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
}
