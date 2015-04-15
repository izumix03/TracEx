package iz.tracex.dto.trac.translate;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author works780
 * 1ファイル、1まとまりの変更履歴
 */
public final class SvnDiff {
	private long preStartLines;
	private long aftStartLines;
	private List<String> preCodes = new ArrayList<String>();
	private List<String> aftCodes = new ArrayList<String>();
	
	public String preCodeString;
	public String aftCodeString;
	
	
	public long getPreStartLines() {
		return preStartLines;
	}
	public void setPreStartLines(long preStartLines) {
		this.preStartLines = preStartLines;
	}
	public long getAftStartLines() {
		return aftStartLines;
	}
	public void setAftStartLines(long aftStartLines) {
		this.aftStartLines = aftStartLines;
	}
	public List<String> getPreCodes() {
		return preCodes;
	}
	public List<String> getAftCodes() {
		return aftCodes;
	}
		
	public void addPreCodes(String preCode){
		preCodes.add(preCode);
	}
	
	public void addAftCodes(String aftCode){
		aftCodes.add(aftCode);
	}
	
	public void addBothCodes(String code){
		preCodes.add(code);
		aftCodes.add(code);
	}
	
	public void buildPreCodeString(){
		preCodeString = buildCodeString(preCodes);
	}
	
	public void buildAftCodeString(){
		aftCodeString = buildCodeString(aftCodes);
	}
	
	private String buildCodeString(List<String> codes){
		StringBuilder sb = new StringBuilder();
		for (String s : codes){
				sb.append(s);
		}
		return sb.toString();
	}
	
}
