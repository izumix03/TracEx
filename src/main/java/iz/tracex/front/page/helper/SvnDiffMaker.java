package iz.tracex.front.page.helper;

import iz.tracex.dto.trac.translate.SvnDiff;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.wc.DefaultSVNDiffGenerator;
import org.tmatesoft.svn.core.wc.ISVNDiffGenerator;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SvnDiffMaker {
	private static final String SVN_USER_ID = "izumikawa_t";
	private static final String SVN_USER_PASSWORD = "izumikawa_t";
	private static final String SVN_URL = "http://ccm-server/svn/ccm/";

	private SVNClientManager clientManager;

	/**
	 * コンストラクタ
	 */
	public SvnDiffMaker() {
		// SVNKitを使うための前処理
		init();
		// SVNのClientManagerを生成する
		clientManager = SVNClientManager.newInstance(
				SVNWCUtil.createDefaultOptions(true) // defaultのオプション設定で作成
				, SVN_USER_ID // SVNのユーザID
				, SVN_USER_PASSWORD); // SVNのユーザPASSWORD
	}

	/**
	 * DIffをapache経由で取得する
	 * 
	 * @param fromRev
	 * @param toRev
	 * @param fromPath
	 * @param toPath
	 * @return
	 */
	public List<SvnDiff> getDiff(SVNRevision fromRev, SVNRevision toRev,
			String fromPath, String toPath) {
		SVNDiffClient diffClient = clientManager.getDiffClient();
		ISVNDiffGenerator diffGenerator = new DefaultSVNDiffGenerator();
		diffGenerator.setEncoding("ISO_8859_1");
		diffClient.setDiffGenerator(diffGenerator);

		ByteArrayOutputStream result = new ByteArrayOutputStream();
		try {
			diffClient.doDiff(SVNURL.parseURIEncoded(SVN_URL + fromPath),
					fromRev, SVNURL.parseURIEncoded(SVN_URL + toPath), toRev,
					SVNDepth.getInfinityOrEmptyDepth(true), false, result);

			// SvnDiffDtoリストに詰め直して返す
			String StrResult = new String(result.toByteArray(), "Shift_JIS");
			return convertSvnDiff(StrResult);

		} catch (SVNException svne) {
			svne.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Dtoに詰めて返す。例外がある場合は本来制御したいが、現状はnullで戻す
	 * 
	 * @param string
	 * @return
	 */
	private List<SvnDiff> convertSvnDiff(String diffStr) {
		List<SvnDiff> svnDiffList = new ArrayList<SvnDiff>();

		String[] diffStrs = diffStr.split("\n");
		String str;

		SvnDiff diff = new SvnDiff();

		try{
		for (int i = 0; i < diffStrs.length; i++) {
			// 0~3はファイル情報
			if (i <= 3) {
				continue;
			}

			// 5行目以降
			str = diffStrs[i];

			// 開始→行番号取り出し
			if (i == 4) {
				diff.setPreStartLines(Long.parseLong(str.split(" ")[1]
						.split(",")[0].replace("-", "")));
				diff.setAftStartLines(Long.parseLong(str.split(" ")[2]
						.split(",")[0].replace("+", "")));
				continue;
			}

			// 新ブロック→行番号取り出し
			if (str.startsWith("@@")) {// && str.endsWith("@@\\r")){
				diff.buildAftCodeString();
				diff.buildPreCodeString();
				svnDiffList.add(diff);

				diff = new SvnDiff();

				diff.setPreStartLines(Long.parseLong(str.split(" ")[1]
						.split(",")[0].replace("-", "")));
				diff.setAftStartLines(Long.parseLong(str.split(" ")[2]
						.split(",")[0].replace("+", "")));
				continue;
			}

			// 文字列の処理
			if (str.startsWith("+")) {
				// aftに追加
				diff.addAftCodes(str.replace("+", " "));
				continue;
			} else if (str.startsWith("-")) {
				// preに追加
				diff.addPreCodes(str.replace("-", " "));
				continue;
			} else {
				// 両方に追加
				diff.addBothCodes(str);
				continue;
			}

		}
		diff.buildAftCodeString();
		diff.buildPreCodeString();
		svnDiffList.add(diff);
		return svnDiffList;
		}catch (Exception e){
			return svnDiffList;
		}
	}

	/**
	 * APIを使用する際の事前準備
	 */
	private void init() {
		SVNRepositoryFactoryImpl.setup();
		DAVRepositoryFactory.setup();
	}

}
