package idv.funnybrain.plurkchat.data;

/**
 * Created by Freeman on 2014/4/3.
 * 'en': 'English'
 'pt_BR': 'Português'
 'cn': '中文 (中国)'
 'ca': 'Català'
 'el': 'Ελληνικά'
 'dk': 'Dansk'
 'de': 'Deutsch'
 'es': 'Español'
 'sv': 'Svenska'
 'nb': 'Norsk bokmål'
 'hi': 'Hindi'
 'ro': 'Română'
 'hr': 'Hrvatski'
 'fr': 'Français'
 'ru': 'Pусский'
 'it': 'Italiano '
 'ja': '日本語'
 'he': 'עברית'
 'hu': 'Magyar'
 'ne': 'Nederlands'
 'th': 'ไทย'
 'ta_fp': 'Filipino'
 'in': 'Bahasa Indonesia'
 'pl': 'Polski'
 'ar': 'العربية'
 'fi': 'Finnish'
 'tr_ch': '中文 (繁體中文)'
 'tr': 'Türkçe'
 'ga': 'Gaeilge'
 'sk': 'Slovenský'
 'uk': 'українська'
 'fa': 'فارسی
 */
public enum Language {
    EN("en"),
    PT_BR("pt_BR"),
    CN("cn"),
    CA("ca"),
    EL("el"),
    DK("dk"),
    DE("de"),
    ES("es"),
    SV("sv"),
    NB("nb"),
    HI("hi"),
    RO("ro"),
    HR("hr"),
    FR("fr"),
    RU("ru"),
    IT("it"),
    JA("ja"),
    HE("he"),
    HU("hu"),
    NE("ne"),
    TH("th"),
    TA_FP("ta_fp"),
    IN("in"),
    PL("pl"),
    AR("ar"),
    FI("fi"),
    TR_CH("tr_ch"),
    TR("tr"),
    GA("ga"),
    SK("sk"),
    UK("uk"),
    FA("fa");

    private String language;

    Language(String language) {
        this.language = language;
    }

    public static Language getLang(String lang) {
        for(Language l : Language.values()) {
            if(l.toString().equals(lang)) {
                return l;
            }
        }
        return Language.EN;
    }


    @Override
    public String toString() {
        return language;
    }
}
