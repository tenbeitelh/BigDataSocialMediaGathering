package de.text.tokenizator;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.de.GermanLightStemFilter;
import org.apache.lucene.analysis.de.GermanNormalizationFilter;
import org.apache.lucene.analysis.de.GermanStemFilter;
import org.apache.lucene.analysis.miscellaneous.SetKeywordMarkerFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.std40.StandardTokenizer40;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.analysis.util.WordlistLoader;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.Version;


/**
 * Custom Implemenation of the GermanAnalyzer of Lucence v5. It is necessary to
 * get controll of the stemming and tokenization process
 * 
 * @author H. Tenbeitel
 *
 */
public final class GermanAnalyzer extends StopwordAnalyzerBase {

	/** File containing default German stopwords. */
	public final static String DEFAULT_STOPWORD_FILE = "german_stop.txt";

	private boolean lowerCase = false;
	private boolean stemming = false;
	private boolean normalization = false;
	private boolean keyword = false;
	private boolean stopWord = false;
	private StemmingType stemmingType = StemmingType.GermanLightStemFilter;

	/**
	 * Returns a set of default German-stopwords
	 * 
	 * @return a set of default German-stopwords
	 */
	public static final CharArraySet getDefaultStopSet() {
		return DefaultSetHolder.DEFAULT_SET;
	}

	private static class DefaultSetHolder {
		private static final CharArraySet DEFAULT_SET;

		static {
			try {
				DEFAULT_SET = WordlistLoader.getSnowballWordSet(
						IOUtils.getDecodingReader(SnowballFilter.class, DEFAULT_STOPWORD_FILE, StandardCharsets.UTF_8));
			} catch (IOException ex) {
				// default set should always be present as it is part of the
				// distribution (JAR)
				throw new RuntimeException("Unable to load default stopword set");
			}
		}
	}

	/**
	 * Contains the stopwords used with the {@link StopFilter}.
	 */

	/**
	 * Contains words that should be indexed but not stemmed.
	 */
	private final CharArraySet exclusionSet;

	/**
	 * Builds an analyzer with the default stop words:
	 * {@link #getDefaultStopSet()}.
	 */
	public GermanAnalyzer() {
		this(DefaultSetHolder.DEFAULT_SET);
	}

	/**
	 * Builds an analyzer with the given stop words
	 * 
	 * @param stopwords
	 *            a stopword set
	 */
	public GermanAnalyzer(CharArraySet stopwords) {
		this(stopwords, CharArraySet.EMPTY_SET);
	}

	/**
	 * Builds an analyzer with the given stop words
	 * 
	 * @param stopwords
	 *            a stopword set
	 * @param stemExclusionSet
	 *            a stemming exclusion set
	 */
	public GermanAnalyzer(CharArraySet stopwords, CharArraySet stemExclusionSet) {
		super(stopwords);
		exclusionSet = CharArraySet.unmodifiableSet(CharArraySet.copy(stemExclusionSet));
	}

	/**
	 * Creates {@link org.apache.lucene.analysis.Analyzer.TokenStreamComponents}
	 * used to tokenize all the text in the provided {@link Reader}.
	 * 
	 * @return {@link org.apache.lucene.analysis.Analyzer.TokenStreamComponents}
	 *         built from a {@link StandardTokenizer} filtered with
	 *         {@link StandardFilter}, {@link LowerCaseFilter},
	 *         {@link StopFilter} , {@link SetKeywordMarkerFilter} if a stem
	 *         exclusion set is provided, {@link GermanNormalizationFilter} and
	 *         {@link GermanLightStemFilter}
	 */
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		final Tokenizer source;
		if (getVersion().onOrAfter(Version.LUCENE_4_7_0)) {
			source = new StandardTokenizer();
		} else {
			source = new StandardTokenizer40();
		}
		TokenStream result = new StandardFilter(source);
		if (lowerCase) {
			result = new LowerCaseFilter(result);
		}
		if (stopWord) {
			result = new StopFilter(result, stopwords);
		}
		if (keyword) {
			result = new SetKeywordMarkerFilter(result, exclusionSet);
		}
		if (stemming) {
			switch (stemmingType) {
			case GermanLightStemFilter:
				result = new GermanNormalizationFilter(result);
				break;
			case GermanMinimalStemFilter:
				result = new GermanStemFilter(result);
				break;
			case GermanStemFilter:
				result = new GermanLightStemFilter(result);
				break;
			default:
				result = new GermanLightStemFilter(result);
				break;
			}
		}

		return new TokenStreamComponents(source, result);
	}

	public boolean isLowerCase() {
		return lowerCase;
	}

	public boolean isStemming() {
		return stemming;
	}

	public boolean isNormalization() {
		return normalization;
	}

	public boolean isKeyword() {
		return keyword;
	}
	
	public boolean isStopWord(){
		return stopWord;
	}

	public void enableLowerCase() {
		lowerCase = true;
	}

	public void disableLowerCase() {
		lowerCase = false;
	}

	public void enableStemming(StemmingType type) {
		stemming = true;
		stemmingType = type;
	}

	public void diableStemming() {
		stemming = false;
		stemmingType = StemmingType.GermanLightStemFilter;
	}

	public void enableNormalization() {
		normalization = true;
	}

	public void disableNormalization() {
		normalization = false;
	}

	public void enableKeyword() {
		keyword = true;
	}

	public void diableKeyword() {
		keyword = false;
	}

	public void enableStopWords() {
		stopWord = true;
	}

	public void disableStopWords() {
		stopWord = false;
	}

}