package edu.uoc.ictflag.helpers.PilotConfiguration;

import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupPage;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupRolePageAccess;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.CourseGroup;
import edu.uoc.ictflag.institution.model.CourseGroupMember;
import edu.uoc.ictflag.security.model.Page;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;
import edu.uoc.ictflag.setup.core.DbSetupHelper;

public class PilotConfiguration
{
	private static EntityManager em;
	
	public static void main(String[] args)
	{
		try
		{
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("edu.uoc.ictflag.helpers.PilotConfiguration");
			
			em = emf.createEntityManager();
			
			DbSetupHelper.setEntityManager(em);
			
			DbSetupHelper.startTransaction();
			
			configurePilot();

			DbSetupHelper.commitTransaction();
		}
		catch (Exception e)
		{
			if (em != null && em.getTransaction() != null)
			{
				em.getTransaction().rollback();
			}
			
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
		}
	}
	
	private static String[][] students =
	{
					{ "rcaselles", "434010", "ca", "p7ACraPr" }, { "jramirezbas", "602833", "ca", "za5ecetR" },
					{ "jlloses", "621030", "ca", "8raHacHE" }, { "cgarciavila", "555075", "ca", "jA3rutrA" },
					{ "xnavarrob", "496821", "ca", "3AchewEs" }, { "jariasgo", "678325", "ca", "Fad8seCr" },
					{ "francescmr81", "569139", "ca", "thuDa5uj" }, 
					{ "jrodriguezgonzalez2", "687784", "ca", "tR5qaDEf" }, { "lbalaguerf", "753609", "ca", "yufRaph2" },
					{ "noljevic", "753315", "ca", "VuqUsp5p" }, { "mfmax", "750633", "ca", "5AkUpreP" }, { "jsoleclo", "591836", "ca", "n7vuPege" },
					{ "jpome", "224221", "ca", "pu4enavE" }, { "xsimonav", "398141", "ca", "jAgu5EtU" }, { "osimon", "524778", "ca", "wrexAv2p" },
					{ "jcasamiquelac", "458263", "ca", "PruMewU2" }, { "xjalencas", "774557", "ca", "guph7drE" },
					{ "xose", "788330", "ca", "4eThedru" }, { "jgavilang", "751774", "ca", "wruc8fAy" }, { "manelpueyo", "424886", "ca", "bRumeN4w" },
					{ "mgonzalezbas", "443508", "ca", "sax5huwE" }, { "ddiazsi", "683071", "ca", "w5kenAha" },
					{ "mmaranonv", "754247", "ca", "MexevA7r" }, { "gfamada", "831779", "ca", "phaP5aku" },
					{ "jvandellosc", "810433", "ca", "vuwUde2E" }, { "avilape", "830949", "ca", "YaYusw5p" },
					{ "ccastillo23", "823806", "ca", "thEphuc6" }, { "jortizcastro", "831257", "ca", "m5ceNufR" },
					{ "gbadiari", "779813", "ca", "3UXawudr" }, { "oterrero", "842974", "ca", "tHamasa2" },
					{ "dguarinom", "837976", "ca", "4EjusTeP" }, { "driosbe", "836743", "ca", "swuc8Ada" },
					{ "jordiberenguervi", "639799", "ca", "cumath2S" }, { "ma202mmd", "813909", "ca", "VAQake8u" },
					{ "vmanaresa", "854640", "ca", "s2upRAku" }, { "dhernandezcal", "864569", "ca", "6ADrepep" },
					{ "vicenc", "611271", "ca", "R4wrukak" }, { "anecsoiu", "850402", "ca", "hUTra3aQ" },
					{ "cduranmolina", "862890", "ca", "t4asTasu" }, { "drivasa", "864603", "ca", "jEDru4rU" },
					{ "sazouache", "863006", "ca", "cAthUpu7" }, { "bilalb", "859494", "ca", "5ufRUnes" }, { "rbracke", "827787", "ca", "breDr6fe" },
					{ "apuiggari", "398736", "ca", "vas5EXuY" }, { "dobleamarilla", "861759", "ca", "spuREt8e" },
					{ "jpuigvig", "848958", "ca", "brApa4uy" }, { "vvichev", "863946", "ca", "8puSumab" },
					{ "hel_hmidi", "859498", "ca", "daT6Udre" }, { "mpasole", "858372", "ca", "vuTr5Kes" },
					{ "jordipamies", "859547", "ca", "SpaVU4ut" }, { "fllovet", "678420", "ca", "femep7eS" },
					{ "sferreirao", "822530", "ca", "GecH7cru" }, { "utena", "853005", "ca", "masExEb7" },
					{ "mmartinm777", "824083", "ca", "4respUsW" }, { "bal-noumair", "848719", "ca", "pha7rupE" },
					{ "sanllue", "855477", "ca", "xet2phAP" }, { "aaylagasg", "875222", "ca", "FRAtrap5" },
					{ "necorage", "481789", "ca", "8rusPacE" }, { "ebaron11", "858224", "ca", "dedU2was" },
					{ "chidalgomar", "862397", "ca", "7hUTreza" }, { "mtovar", "874643", "ca", "7ephapRu" },
					{ "ghuertas1", "820522", "ca", "bRudrAp8" }, { "marccabre", "876959", "ca", "fedRU6ed" },
					{ "mjodarb", "877585", "ca", "SwusweP2" }, { "fpay", "858343", "ca", "cHay8Phe" }, { "fgalad", "881515", "ca", "5ruXEtha" },
					{ "ralomar", "148333", "ca", "Pruwru6a" }, { "perugachialvear", "831551", "ca", "franaBR2" },
					{ "gaznarj", "777179", "ca", "ceZuRu4E" }, { "jparnau", "239777", "ca", "pEVuqAk4" }, { "ffranco", "181649", "ca", "prarEg6p" },
					{ "jmaciasdo", "808742", "ca", "g4XeTHac" }, { "perebiosca", "879172", "ca", "5epUgesu" },
					{ "cramosac", "544042", "ca", "VeceTAt2" }, { "ibullich", "291340", "ca", "zaFA3aya" },
					{ "mcatalava", "495471", "ca", "7wAStaCe" }, { "iguere", "642932", "ca", "rA3eDewe" }, { "lmeliani", "556453", "ca", "dra2RuxU" },
					{ "gbalaguerr", "874399", "ca", "stedAju5" }, { "amartisa42", "821872", "ca", "3rufrEfr" },
					{ "davidcenzual", "875257", "ca", "naWrE6ut" }, { "abadjim", "830658", "ca", "2etrEBre" },
					{ "jvillegasp", "903156", "ca", "6rAspExu" }, { "agarciamartinez0", "454906", "ca", "Pa3eswew" },
					{ "yrueda", "588409", "ca", "fru5umaC" }, { "jmunt", "152788", "ca", "M4guYest" }, { "abenitomarc", "779140", "ca", "bragAx3B" },
					{ "evidalros", "911399", "ca", "cRA8ucru" }, { "vbano", "791950", "ca", "dr3chuTh" },
					{ "cgarciaperez0123", "899908", "ca", "f3ukamuV" }, { "miquelfa", "882548", "ca", "3UpHEsup" },
					{ "nmenia", "891447", "ca", "p8SwaDru" }, { "jmayolba", "309927", "ca", "6abeFUFu" }, { "jdiazv", "193557", "ca", "Ph5wrake" },
					{ "igonzalezi", "209546", "ca", "5EtreKas" }, { "adonjo", "154107", "ca", "cru4HaHe" }, { "breal", "434439", "ca", "Macucre7" },
					{ "jserranoru", "396611", "ca", "Fab6pukE" }, { "xbenavent", "498927", "ca", "beRes6xa" },
					{ "aauli", "430507", "ca", "Cresw2nA" }, { "fgomezh", "504377", "ca", "3Exatrad" }, { "mmunozgonz", "913749", "ca", "XamedRA2" },
					{ "avilaltaga", "437853", "ca", "qa2aQetE" }, { "ovidalj", "912180", "ca", "wepreqE3" },
					{ "rruizcan", "911040", "ca", "chUmU7ad" }, { "paumaresma", "909706", "ca", "sabu5agE" },
					{ "davidpardolo", "910426", "ca", "v6YaCHub" }, { "aferrandolavila", "906074", "ca", "4umuQatu" },
					{ "jborrasr", "892810", "ca", "paF3spug" }, { "cnunezfer", "844522", "ca", "Qe6utrAz" },
					{ "rmirallesmo", "913738", "ca", "7UCruvas" }, { "imartinlop", "913424", "ca", "Swus5exu" },
					{ "jcgarcia", "900164", "ca", "tr7peswE" }, { "ebadillog", "786804", "ca", "Ku3uvufa" },
					{ "marcrdvs", "911317", "ca", "ph8BRePr" }, { "egill31", "848879", "ca", "Y8tacHuj" },
					{ "bbellmunt", "902098", "ca", "6Enachat" }, { "mrocabib", "858153", "ca", "gase2Ehe" },
					{ "azorrillas", "895500", "ca", "creYE8af" }, { "ezequielmejor", "895723", "ca", "SpusW5vA" },
					{ "jperezlap", "834125", "ca", "haG3sWEs" }, { "vgimenos", "838677", "ca", "k6zukUtr" },
					{ "jfloresde", "907533", "ca", "pUs7ePra" }, { "cvilardellg", "859067", "ca", "5epRedrE" },
					{ "lgarridosa", "900288", "ca", "6rutUyas" }, { "eleonmart", "900684", "ca", "Tewru8re" },
					{ "joamajor", "901359", "ca", "j4TawrAb" }, { "nataliapadilla", "888947", "ca", "Tus6uxeV" },
					{ "jordijim", "876704", "ca", "tAtramA3" }, { "marc25219", "903582", "ca", "Fr8garub" },
					{ "emonfortb", "904800", "ca", "th3CathU" }, { "jmmesa", "842403", "ca", "ThU7Ecec" },
					{ "gcarmonal", "890343", "ca", "5uzanePH" }, { "jhiroshi", "897642", "ca", "xAbr2Waf" },
					{ "cristinaruiz", "879029", "ca", "Ceq4GEve" }, { "jmedinavi", "589172", "ca", "venUP4uF" },
					{ "agallartv", "891976", "ca", "th5Caswu" }, { "dgarciaho", "891860", "ca", "SweS5Upu" }, { "rlar", "891730", "ca", "cHepr5va" },
					{ "jforerot", "898588", "ca", "bAs7cruh" }, { "fpedregal", "894402", "ca", "qAhE6Uxe" },
					{ "gfandos", "874029", "ca", "fUKe7rup" }, { "erikmarimon", "769330", "ca", "8apefEwe" },
					{ "jgalanr", "889118", "ca", "DracruP4" }, { "jfelipz", "874213", "ca", "b4achePr" }, { "miniaoshi", "912273", "ca", "baWepeb6" },
					{ "fbouro", "773759", "ca", "Wrup7esW" }, { "aregueira", "904961", "ca", "4AsEphAb" },
					{ "ddomenechco", "849686", "ca", "p3prAGEr" }, { "gquinones", "913481", "ca", "fuWrep2a" },
					{ "a3chango", "652770", "ca", "brA7ecru" }, { "jhurtadogr", "444664", "ca", "t5usTere" },
					{ "xmestreg", "776668", "ca", "cr4Phatu" }, { "annexa", "876848", "ca", "b3uHEpRa" }, { "agaspark", "883681", "ca", "s4etAjab" },
					{ "jpagesg", "754539", "ca", "sudES3Eg" }, { "raul_medina", "650507", "ca", "banuS2ep" },
					{ "fserrats", "690527", "ca", "bejUqEs7" }, { "arodriguezalv", "412148", "ca", "pr5CeJuP" },
					{ "jlra5", "430683", "ca", "FaPha5er" }, { "bmartinezgar", "691022", "ca", "GE2uphej" },
					{ "rruizmata", "910535", "ca", "prakU5ta" }, { "mcarresr", "697987", "ca", "WrUg4bah" },
					{ "cnaveda", "912527", "ca", "Mebru3ud" }, { "grotgers", "878101", "ca", "CRetas4a" }, { "oandresi", "906389", "ca", "crEb4swE" },
					{ "lventurari", "892969", "ca", "ThabeD6a" }, { "ddeber", "518905", "ca", "g2nuSupH" },
					{ "mmomplet", "913884", "ca", "drUd8uDu" }, { "xaviergarciapuig", "535579", "ca", "gewreCu3" },
					{ "amontesar", "827015", "ca", "dr3Spust" }, { "jbadet", "894885", "ca", "5rumEbuc" }, { "jvernett", "895325", "ca", "Faswedr5" },
					{ "raulferra", "817675", "ca", "4uPhedru" }, { "rfontsere", "569205", "ca", "muD5fefE" },
					{ "abusquetss", "903454", "ca", "je2aSwuw" }, { "jvallsroi", "876288", "ca", "ve5hetrA" },
					{ "aboira", "897007", "ca", "Yuq6wuCa" }, { "operezcast", "895292", "ca", "3remeQay" }, { "cilea", "910156", "ca", "Ja3ezAru" },
					{ "fsolanog", "874847", "ca", "wrepH4pU" }, { "acarracedou", "901440", "ca", "baCr6tuR" },
					{ "gariasf", "896732", "ca", "spUvAsp4" }, { "fgaleraf", "897271", "ca", "p5uTHesw" },
					{ "danielbelzunce", "842099", "ca", "pUbRutr3" }, { "imarzo", "891018", "ca", "zaSp6sta" },
					{ "xrincon", "893094", "ca", "prEQUdr6" }, { "jordiszamorano", "570537", "ca", "du5EChab" },
					{ "sstefanczyk", "851967", "ca", "Zu4ucHat" }, { "pfuertesc", "890212", "ca", "3rudecEB" },
					{ "fgonzalezle", "897075", "ca", "wU2uSuth" }, { "aolivareso", "822039", "ca", "zEth8thE" },
					{ "afernandezesteves", "837411", "ca", "4UqakufR" }, { "afernandezcur", "898153", "ca", "p5arEsuQ" },
					{ "aantonpan", "878651", "ca", "hU6HadAs" }, { "carlesgrauvila", "891166", "ca", "Hu8ayepu" },
					{ "jvivasa", "589191", "ca", "vaTamec6" }, { "bmaner", "880365", "ca", "cugupeC8" }, { "gromay", "892748", "ca", "Racus3EB" },
					{ "cmoralesmu", "906807", "ca", "cHaPrU5r" }, { "agiroq", "857025", "ca", "x2Cuhuqu" },
					{ "dculebradas", "607050", "ca", "fru3wUtR" }, { "x8124903", "856379", "ca", "3TaphExu" },
					{ "acarrilloca", "896089", "ca", "Swubax8x" }, { "dbarrero", "791350", "ca", "CA2ufruc" },
					{ "ebt306", "788405", "ca", "sPuWA4at" }, { "robertmb", "876471", "ca", "Chapr2sT" },
					{ "rrodriguezlopez01", "903296", "ca", "navEd3na" }, { "ralonsoma", "780220", "ca", "GUqAst4t" },
					{ "jonathanasensio87", "898972", "ca", "PreBeSw7" }, { "ldiaz2", "529357", "ca", "QAw2tefa" },
					{ "cinta_sf", "413086", "ca", "wres3UmU" }, { "javigarr", "473351", "ca", "jej8thaP" },
					{ "jjruizag", "533810", "ca", "f3ahAjuf" }, { "aventurap", "911030", "ca", "Druwuva6" },
					{ "srubioma", "757115", "ca", "cuwrE2ap" }, { "mvidal012", "899741", "ca", "th6jaPAd" },
					{ "rgarcia256", "880617", "ca", "sWupuz6C" }, { "gdiezve", "727497", "ca", "Theh6chu" },
					{ "gdomin88", "911158", "ca", "phAth3tA" }, { "jramonedam", "894778", "ca", "thaXud3s" },
					{ "llopezdemi", "829542", "ca", "vavejA8T" }, { "pplanasf", "902136", "ca", "zEc7ajuS" },
					{ "jbevilacqua", "665308", "ca", "chuSw5pa" }, { "abuitragol", "877620", "ca", "bubA7utr" },
					{ "wchu", "632753", "ca", "6EphUnej" }, { "martinrzco", "836776", "ca", "fuyESu8a" }, { "asalacam", "753389", "ca", "Wa4upred" },
					{ "aelaissaoui", "897310", "ca", "nax4wrUN" }, { "ecarracedo", "891878", "ca", "xuBrePa8" },
					{ "ozabala", "898105", "ca", "fR2vusPa" }, { "acaudevillap", "858143", "ca", "w2Eswuke" },
					{ "vgomezgon", "859502", "ca", "crAp7ubu" }, { "alexcampayo", "909741", "ca", "Cru7resw" },
					{ "rcoma9", "876759", "ca", "3AdeduzE" }, { "inavarrova", "897724", "ca", "beHEx4vu" },
					{ "mferrerfo", "904920", "ca", "FruSep6A" }, { "yviveros", "891131", "ca", "brEq6SuT" }, { "jaiss", "895913", "ca", "f7aQAbra" },
					{ "jlopezlopez", "904229", "ca", "z2zaCUme" }, { "jperezjor", "837920", "ca", "yeqaC8hu" },
					{ "dtelleslopez", "890669", "ca", "CawEsp5y" }, { "gmoreg", "891792", "ca", "stUc8ucu" },
					{ "galbertb", "855022", "ca", "xE5uBasA" }, { "nestanyol", "823570", "ca", "steg3neN" },
					{ "jgonzalezsalv", "899461", "ca", "r5MedUsA" }, { "efigueirasg", "882743", "ca", "SWe4rupe" },
					{ "rfakih", "909234", "ca", "prUm2swa" }, { "rubenminano", "900769", "ca", "c4apuWre" },
					{ "xvicenteo", "901024", "ca", "2EsarUxa" }, { "vperezvil", "901807", "ca", "zAchuh6s" },
					{ "dcarrascoga", "858988", "ca", "5rumuRek" }, { "mjustca", "790889", "ca", "PruP2use" },
					{ "blorenzoch", "575692", "ca", "prEWr5St" }, { "farpi", "784624", "ca", "k4wuxESw" },
					{ "amartinezmolina", "725222", "ca", "seyEra8a" }, { "jmuriav", "900978", "ca", "CHedr7xa" },
					{ "pcosa", "889090", "ca", "qev6Dutu" }, { "acardellmir", "897146", "ca", "tHaHe6eN" },
					{ "fesnaola", "826721", "ca", "de4RejUP" }, { "paparicioru", "865864", "ca", "v7muSusw" },
					{ "rserranon", "538804", "ca", "gep5ezaC" }, { "jmoragrega", "108932", "ca", "jUt2Ucuv" },
					{ "acorrall", "142027", "ca", "wrUspUm3" }, { "ptetsuo", "516763", "ca", "nazACra3" }, { "lbarja", "377624", "ca", "s8ufruSp" },
					{ "iniebla", "406281", "ca", "techuY7p" }, { "sbalari", "495644", "ca", "st5bradR" }, { "gcastror", "639544", "ca", "rAdrE4pe" },
					{ "antoniocabezas", "906562", "ca", "yeN2habe" }, { "eredondoad", "899089", "ca", "pRu5apRu" },
					{ "slozanocas", "907484", "ca", "naGubRu6" }, { "jnole", "910049", "ca", "rAWagap4" },
					{ "acastellanosca", "893489", "ca", "Dew5uxUd" }, { "bhenao", "902066", "ca", "yEfateY8" },
					{ "xjunyent", "895687", "ca", "xeChec6U" }, { "dchafer", "578081", "ca", "fRa5reva" }, { "hbometon", "776241", "ca", "pHeruyU6" },
					{ "bbaulenas", "891884", "ca", "S6AMakes" }, { "feexpositoar", "897377", "ca", "Cevu2Aqe" },
					{ "oconesama", "883552", "ca", "jUbRAcu8" }, { "vcepedam", "909195", "ca", "keGec3an" },
					{ "danimf44", "889724", "ca", "nE3hedEP" }, { "rponsgu", "889255", "ca", "th6trewA" }, { "xguell91", "897618", "ca", "Guswaw6f" },
					{ "lpradosl", "902935", "ca", "zabr2cUp" }, { "jcorraldi", "824770", "ca", "6rarageT" },
					{ "jotalora", "863393", "ca", "7ruyegeS" }, { "kdegamboa", "892132", "ca", "frugAf6u" },
					{ "rgorgas", "889916", "ca", "VeW4usWa" }, { "jcaballero0", "903886", "ca", "Sp8wrebA" },
					{ "jboludab", "890199", "ca", "pAtr3Wru" }, { "vgonzalezalv", "907241", "ca", "pas6brEq" },
					{ "dvazquezfer", "907496", "ca", "BA8wubuK" }, { "rquerolc", "894343", "ca", "b4utrApE" },
					{ "lbarbaf", "893498", "ca", "sw4qEpra" }, { "rtostado", "900442", "ca", "Wefu7hes" }, { "aperezg2", "885176", "ca", "DRubr3ga" },
					{ "fcarrerar", "894462", "ca", "phu2RePH" }, { "nereaa", "901319", "ca", "prUseX8s" }, { "cfalconm", "769627", "ca", "s6crAbaz" },
					{ "sumitpal", "901746", "ca", "wUf2tref" }, { "guiloga", "912476", "ca", "prUtrec3" }, { "faros", "889602", "ca", "2agESech" },
					{ "siglesiasd", "635349", "ca", "qExUs8ah" }, { "drosellopa", "529942", "ca", "cH3rufEq" },
					{ "dgordillom", "597835", "es", "Pr5pRuKe" }, { "fpoloro", "756517", "es", "gAm5ZAna" },
					{ "rmateoma", "760622", "es", "Nef2afaz" }, { "ftristancho", "754362", "es", "DrAph8qe" },
					{ "lmerinog", "284661", "es", "CRerefr6" }, { "amartin41", "792903", "es", "3ratrAwr" },
					{ "chonoviedo", "576952", "es", "huZe3ruz" }, { "aaroncastro", "811002", "es", "5aSefUza" },
					{ "dbasanta", "823301", "es", "xebrAwR3" }, { "jpaz", "586582", "es", "z8sebUst" }, { "davidlf13", "827364", "es", "traphE2U" },
					{ "jmeleroh", "516830", "es", "M7penehE" }, { "fsotog", "857629", "es", "Spanac6a" }, { "ccoronel", "430242", "es", "HekUtre8" },
					{ "fel_harche", "836132", "es", "phuf2DAp" }, { "ypalmas", "864255", "es", "Swat5epr" },
					{ "rogerbg", "854143", "es", "GeJ7jefe" }, { "pbriales", "857467", "es", "2EQesuTh" },
					{ "operezrui", "858094", "es", "vuch7dEz" }, { "jescobaral", "861742", "es", "tUfrevu5" },
					{ "jgarciamoreno2", "863141", "es", "PesTuhu8" }, { "mnader", "844252", "es", "7ehasasT" },
					{ "mvillalongall", "866096", "es", "t5stusuK" }, { "javier_garcia", "850583", "es", "meFr6bup" },
					{ "fzamarbide", "861974", "es", "JAwru5te" }, { "amerinoal", "878906", "es", "5eyESuCe" },
					{ "ibanuls", "587876", "es", "y3DuSpat" }, { "demperador", "885899", "es", "FR6tRuth" },
					{ "averdugoca", "593363", "es", "pUD4aqUp" }, { "arossir", "821832", "es", "XatraHe7" },
					{ "agomezsoto", "592687", "es", "Spaqudr8" }, { "sfem", "830637", "es", "p5uqeWre" }, { "ilahozc", "879923", "es", "c6ThUmaX" },
					{ "ypradar", "735401", "es", "c6aJathu" }, { "cramirezbe", "825304", "es", "FruX7Phu" },
					{ "nsharif", "870169", "es", "cRUfr5za" }, { "spenalbae", "858263", "es", "2uBAkuFr" }, { "ycherad", "875070", "es", "pHuvac4e" },
					{ "acarvajalc", "884214", "es", "ze7reWra" }, { "abparedes76", "906014", "es", "che7uSpu" },
					{ "jalanon", "906351", "es", "vU2awUfu" }, { "gabadac", "914405", "es", "jatH5jEt" },
					{ "fferrerfer", "911106", "es", "dE2ephUk" }, { "rgarcime", "909044", "es", "pexu8EbA" },
					{ "jrodrigue1459", "906603", "es", "nec6UxUv" }, { "amunozguerrero", "913769", "es", "va3eSuHa" },
					{ "carlosvalterna", "901710", "es", "jEvEJ2ch" }, { "ftineoo", "914859", "es", "Spay8pes" },
					{ "jperezmaro", "911651", "es", "cR5CedaP" }, { "izabaleta", "909546", "es", "ZaceCa5u" },
					{ "amourullo", "910473", "es", "Wr4gucra" }, { "jmanzano130", "912274", "es", "tHuka6eg" },
					{ "cblancog", "914178", "es", "waGa4tuv" }, { "jsilvar", "438049", "es", "X2fReyac" }, { "ivanabad", "900028", "es", "c6ezEgaz" },
					{ "jmartmal", "882285", "es", "huHeCr7s" }, { "rfergon", "906875", "es", "yufrabR8" }, { "vmsarrio", "893684", "es", "NeQaceG6" },
					{ "jdiazcastro", "910370", "es", "Cranu2Us" }, { "upereyra", "908372", "es", "t3geWRUd" },
					{ "ciller", "909736", "es", "6rUcefru" }, { "javierpcr", "900521", "es", "vane7rUb" }, { "albertoc", "911289", "es", "wEzEc4ek" },
					{ "jgr", "895018", "es", "fRacr4Br" }, { "abelnaharro", "895367", "es", "Spe8huTr" }, { "dossorio", "896176", "es", "Sw2prape" },
					{ "fernandosierra", "896274", "es", "va6tadeC" }, { "lvicent", "896818", "es", "K2fAjEtr" },
					{ "bertinviqueira", "859769", "es", "swexE2et" }, { "adriancolom", "897679", "es", "c2AWaqUf" },
					{ "pcaiceo", "907710", "es", "meph6gEj" }, { "jhoock", "884803", "es", "xEdabrU3" },
					{ "emaldonadoa", "900834", "es", "XuPRev8k" }, { "dcostab", "870257", "es", "DAc2ebrA" },
					{ "ljimenezmon", "884782", "es", "tUSEvac5" }, { "josem1720", "900781", "es", "c3afuwRU" },
					{ "angellgallego", "885376", "es", "daBrafU8" }, { "apinom", "895288", "es", "drEzuc6a" },
					{ "jfaria", "901598", "es", "ZEtrEfr3" }, { "jpcollado", "896703", "es", "vaz3Sagu" },
					{ "vmartinezal", "852203", "es", "Qubuk5sw" }, { "vmedinap", "903125", "es", "yab5Este" },
					{ "jcorralesp", "903955", "es", "teBat6at" }, { "hcuestag", "890307", "es", "yAS2uSuv" },
					{ "rgomezfern", "890109", "es", "7ruwapuB" }, { "framirezfe", "890695", "es", "s2UCamet" },
					{ "mmarbelmontegonz", "890809", "es", "zesWuvu5" }, { "gmellizo-soto", "897764", "es", "v6spEbuj" },
					{ "lmolla", "879720", "es", "8efucebU" }, { "jinfantesc", "902809", "es", "Br2spUph" },
					{ "paumontoya", "890406", "es", "s6EcREge" }, { "jsoleru", "721144", "es", "f7rajetE" },
					{ "mabegil", "842657", "es", "5AvecUxU" }, { "patxidiaz", "900066", "es", "7rEcaphE" }, { "dgmelon", "910220", "es", "stAcuf3e" },
					{ "jmaresmo", "856771", "es", "spa4RazA" }, { "agarciasanchez1982", "885299", "es", "Re3unexa" },
					{ "mvidagany", "911923", "es", "mEdu5TEk" }, { "mgoizueta_", "607330", "es", "Gewru4UW" },
					{ "asalgadosan", "497195", "es", "thenew4T" }, { "jescalerag", "911941", "es", "D8jereGU" },
					{ "mrrodri29", "896948", "es", "dadrAf5U" }, { "dmarinova", "907706", "es", "C2unerap" },
					{ "pestevezes", "875077", "es", "SU3Ethuk" }, { "mmartinezdecastilla", "891429", "es", "tu2Ruche" },
					{ "jvazquezmon", "891877", "es", "Ch3kegen" }, { "fgomez675", "892809", "es", "Fufr4juc" },
					{ "neussamir", "834529", "es", "4apraqaF" }, { "jvillaltae", "563036", "es", "t3DRameG" },
					{ "nborgesd", "849119", "es", "huwafu8P" }, { "ggomezmarti", "896204", "es", "ve5TExeP" },
					{ "adaunis", "833334", "es", "sUqu3tAs" }, { "alacal", "516583", "es", "DAPupa7e" }, { "ljdb", "894873", "es", "sAp7zurE" },
					{ "asarti", "418913", "es", "Fr7Rubra" }, { "ddavila", "893558", "es", "baje4eYu" }, { "jbenitocas", "452157", "es", "3hacewEs" },
					{ "apardohe", "912397", "es", "b7RetraB" }, { "abeunza", "482638", "es", "Ra5rudra" }, { "sroncero", "914647", "es", "SPuch8br" },
					{ "ifernandez89", "914531", "es", "zEbeHEg3" }, { "afgaldo", "908137", "es", "zuy3deCH" },
					{ "fbagurc", "756651", "es", "bufeZ4th" }, { "jcabrerat", "905665", "es", "4eTetraP" },
					{ "jcastrope", "906629", "es", "b3naBEqe" }, { "jlosadaf", "907012", "es", "ravE4ePr" },
					{ "monamutees", "907837", "es", "xeqePRu7" }, { "cantonj", "893635", "es", "z7bruCRA" },
					{ "hakarouch", "825559", "es", "Th5necHa" }, { "gcabrerat", "894172", "es", "vebuCR7k" },
					{ "ggutierrezbr", "900572", "es", "Yebrafu7" }, { "aanchuste1", "605206", "es", "fuprAj2k" },
					{ "mcasadoal", "873964", "es", "zuqaDe7u" }, { "oliarte", "894933", "es", "Swe4ephe" },
					{ "lvazquezva", "817998", "es", "yech7NaF" }, { "madasensio", "897034", "es", "JawRuch2" },
					{ "ddelaorden", "897479", "es", "mUspej3p" }, { "pmorenomot", "904188", "es", "Crucru4u" },
					{ "bgonzalezne", "857678", "es", "sw2Kawaj" }, { "smorenofernandez", "899523", "es", "cr4pRUqa" },
					{ "crsolis", "907961", "es", "QeChuxa5" }, { "bbenejam", "910961", "es", "spAfuRE5" },
					{ "davidmendez", "851220", "es", "buwruwR8" }, { "igc23", "897101", "es", "jEs6evaV" },
					{ "ilopezir", "877650", "es", "3EwRufRu" }, { "jasa", "889935", "es", "BrUC5ara" }, { "dmonterope", "904304", "es", "FAwra3uz" },
					{ "jlopezruiz01", "906480", "es", "kaCrab3e" }, { "jperezpar", "905776", "es", "zucra8AJ" },
					{ "jpedrochem", "890363", "es", "8hEbuswU" }, { "emilioperez", "889956", "es", "wuKaBAs6" },
					{ "eruizavia", "895680", "es", "dR4ceCud" }, { "dcedresm", "890381", "es", "qa2AChes" },
					{ "eniglesias", "897978", "es", "bruwas4U" }, { "alvarodh5", "897399", "es", "xaratH5c" },
					{ "rriosbo", "898857", "es", "RAspEsp5" }, { "davidsanzm", "906414", "es", "Mu8ewren" },
					{ "mcorchuelop", "880818", "es", "cHEmEp4p" }, { "pmolinacr", "882215", "es", "Kafa4atr" },
					{ "wcosmedlr", "894405", "es", "3HufUbaW" }, { "mpalaugu", "895538", "es", "nA4edRaj" },
					{ "amarquezj", "901620", "es", "v8preFac" }, { "lponsbarber", "910960", "es", "th6fREcu" },
					{ "juanjimenez", "901263", "es", "spufrA4u" }, { "jalvaro0", "901501", "es", "zag4pExu" },
					{ "mmzurita", "889909", "es", "8uswAcha" }, { "jlopezrodao", "790771", "es", "putRaD6a" },
					{ "mmedinarodrigu", "653403", "es", "swEPhU3u" }, { "famorn", "914174", "es", "cek4waRe" },
					{ "sspang", "899669", "es", "h4PubuTa" }, { "ipenido", "899038", "es", "chaB8Ust" },
					{ "dgonzalezri", "908725", "es", "za6eVusa" }, { "dvillalon", "908872", "es", "Yawrah6v" },
					{ "apenin", "904411", "es", "Swuna2RU" }, { "manuj21", "678208", "es", "p3sweSpU" }, { "gabriel", "620729", "es", "Jespuc5u" },
					{ "lserratr", "911815", "es", "s6rAqupr" }, { "lrol", "365278", "es", "2tawacHu" }, { "jcasero0", "885392", "es", "nEvaBRe6" },
					{ "spenedo", "910043", "es", "sw2trUtr" }, { "mmartinsanchez0", "914900", "es", "3tahaReS" },
					{ "jmorenoso", "911783", "es", "masTup6e" }, { "afrancoss", "863551", "es", "s6tHabra" },
					{ "acanoma", "844941", "es", "wExa5udr" }, { "dmirandatorres", "893531", "es", "paThA6hu" },
					{ "cguti", "894274", "es", "tReChu2e" }, { "ddominguezlo", "894272", "es", "5UpUkeQe" },
					{ "fhernandezvich", "900505", "es", "cus5eNAt" }, { "fborip", "900546", "es", "6eXujabe" },
					{ "elebarra", "692306", "es", "mAb2Ecra" }, { "mtorresgut", "735729", "es", "bA5raQuw" },
					{ "jmoreno99", "895448", "es", "wr7bRuth" }, { "xosecardoso", "874208", "es", "DA7ububu" },
					{ "mgamazo", "890055", "es", "faXEg4Xu" }, { "javisr", "890788", "es", "qEYukaC4" }, { "bpalmer", "897971", "es", "dArEs3Us" },
					{ "amaneiron", "904079", "es", "sadrAza8" }, { "mvillalongavil", "882719", "es", "te2Uvewr" },
					{ "jgomezbj", "906926", "es", "6pUgaqap" }, { "raquelgc", "829760", "es", "c7AcRetR" }, { "dtunez", "884099", "es", "wePrAka3" },
					{ "jprovecho", "901899", "es", "QekU5acu" }, { "tkabbaj", "851788", "es", "7waFRaye" },
					{ "norber145", "896001", "es", "MufafE8U" }, { "jgonzalezcarvajal", "902966", "es", "2ErEqequ" },
					{ "jdelgadoba", "877226", "es", "pHudeth5" }, { "imosquerac", "896929", "es", "drujUqa5" },
					{ "jperezho", "861596", "es", "stewre5E" }, { "gabrielfdez", "889889", "es", "crA6emeb" },
					{ "judbeto", "897288", "es", "the4Hazu" }, { "juangodeba", "793164", "es", "bR5wreka" },
					{ "jmmulero", "904375", "es", "3upreMAk" }, { "eleazrs", "841910", "es", "nA3TEdra" }, { "jgonrec", "899933", "es", "D4edaqep" },
					{ "fmunizfernandez", "899770", "es", "treFrE7a" }, { "alorenzoma", "782700", "es", "PheR5cub" },
					{ "jmdr78", "894360", "es", "7pEsWuBa" }, { "fcounago", "810403", "es", "mutHaB6A" }, { "jfcerdeno", "900694", "es", "pruse6Re" },
					{ "jlopezgr", "810562", "es", "5hECused" }, { "jricoc", "889737", "es", "YawEcr6w" },
					{ "josecsobrino", "895890", "es", "PRu3huch" }, { "ppourtau", "901678", "es", "fruHa7ep" },
					{ "garriaga", "283990", "es", "tAs7hesA" }, { "jblascola", "409727", "es", "k6pRures" },
					{ "mnievesco", "563724", "es", "dR5Swuch" }, { "jvcontel", "858759", "es", "th5cubUp" },
					{ "ocuencal", "890188", "es", "Pra3eswA" }, { "jacasamayor", "754970", "es", "Fuswu6eb" },
					{ "rescobarga", "904712", "es", "brEsPe8R" }, { "efernandezgonza", "539802", "es", "2RUdUtag" },
					{ "anchang", "649683", "es", "dr3MEswe" }, { "lsinusia", "439956", "es", "zus3Aswe" },
					{ "mmedinafer", "898141", "es", "Druchu5a" }, { "leomoyano", "891903", "es", "wewES3aN" },
					{ "imorgil", "662503", "es", "wrama6Ed" },
	};
	
	private static void configurePilot() throws Exception
	{
		// Change passwords
		
		//DbSetupHelper.changeUserPassword("su", "k24OR!le");
		//DbSetupHelper.changeUserPassword("admin", "k24OR!le");
		
		//reConfigurePermissions();
		
		ArrayList<UserRole> roles = new ArrayList<UserRole>();
		roles.add(UserRole.INSTRUCTOR);

		User instructor = DbSetupHelper.getDbHelper().getFirst("SELECT u from User u where u.username = 'fc_instr'", User.class);
				
		if (instructor == null)
		{
			instructor = DbSetupHelper.setupUser("fc_instr", "fc_instr", "th24!FrK", "fc_instr", roles);
		}
		
		Course c1 = DbSetupHelper.getDbHelper().getFirst("SELECT c from Course c where c.code = '05.562 - 2016-1'", Course.class);
		Course c2 = DbSetupHelper.getDbHelper().getFirst("SELECT c from Course c where c.code = '75.562 - 2016-1'", Course.class);
		
		CourseGroup cg1 = DbSetupHelper.setupCourseGroup(LocalizedString.fromStringFormat("ca#05.562 - 2016-1"), c1, instructor);
		CourseGroup cg2 = DbSetupHelper.setupCourseGroup(LocalizedString.fromStringFormat("es#75.562 - 2016-1"), c2, instructor);
	
		roles = new ArrayList<UserRole>();
		roles.add(UserRole.STUDENT);
		
		DbSetupHelper.startTransaction();
		
		for (String [] student : students)
		{
			User std = DbSetupHelper.getDbHelper().getFirst("SELECT u from User u where u.username = '" + student[0] + "'", User.class);
			
			if (std == null)
			{
				std = DbSetupHelper.setupUser(student[0], student[0], student[3], student[0] + "@uoc.edu", roles, student[2]);
			}
			
			std.setIdp(student[1]);
			
			CourseGroupMember cgm = new CourseGroupMember();
			cgm.setUser(std);
			
			if (student[3].equals("ca"))
			{
				cg1.getMembers().add(cgm);
			}
			else
			{
				cg2.getMembers().add(cgm);
			}
		}
		
		DbSetupHelper.commitTransaction();
	}

	private static void reConfigurePermissions() throws Exception
	{
		DbSetupHelper.startTransaction();
		DbSetupHelper.getDbHelper().executeUpdateNative("DELETE FROM UserRolePageAccess", new ArrayList<Object>());
		DbSetupHelper.commitTransaction();
		
		DbSetupHelper.startTransaction();
		DbSetupHelper.getDbHelper().executeUpdateNative("DELETE FROM Pages", new ArrayList<Object>());
		DbSetupHelper.commitTransaction();

		Page p = setupPage("PrivateIndex", "Admin", "core", LocalizedString.fromStringFormat("en#Index"), "/private/index.xhtml", "", -1, false);
		
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		setupRolePageAccess(p, UserRole.LECTURER);
		setupRolePageAccess(p, UserRole.INSTRUCTOR);
		setupRolePageAccess(p, UserRole.STUDENT);
		
		p = setupPage("ProgramsList", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Programs list;ca#Llista de programes;es#Lista de programas"), "/private/ProgramsList.xhtml",
				"fa fa-user-plus", 0, true);
		
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		
		p = setupPage("EditProgram", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Edit program;ca#Editar programa;es#Editar programa"), "/private/EditProgram.xhtml",
				"", -1, false);
		
		setupRolePageAccess(p, UserRole.ADMIN);
		
		p = setupPage("SubjectsList", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Subjects list;ca#Llista d'assignatures;es#Lista de asignaturas"), "/private/SubjectsList.xhtml",
				"fa fa-user-plus", 0, true);
		
		setupRolePageAccess(p, UserRole.ADMIN);
		//setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		//setupRolePageAccess(p, UserRole.LECTURER);
		//setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("EditSubject", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Edit subject;ca#Editar assignatura;es#Editar asignatura"), "/private/EditSubject.xhtml", "", -1,
				false);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		
		p = setupPage("CoursesList", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Courses list;ca#Llista de cursos;es#Lista de cursos"), "/private/CoursesList.xhtml",
				"fa fa-user-plus", 0, true);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		//setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		//setupRolePageAccess(p, UserRole.LECTURER);
		//setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("EditCourse", "Admin", "institution", LocalizedString.fromStringFormat("en#Edit course;ca#Editar curs;es#Editar curso"),
				"/private/EditCourse.xhtml", "", -1, false);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		//setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		
		p = setupPage("CourseGroupsList", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Groups list;ca#Llista de grups;es#Lista de grupos"), "/private/CourseGroupsList.xhtml",
				"fa fa-user-plus", 0, true);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		//setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		//setupRolePageAccess(p, UserRole.LECTURER);
		//setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("EditCourseGroup", "Admin", "institution", LocalizedString.fromStringFormat("en#Edit group;ca#Editar grup;es#Editar grupo"),
				"/private/EditGroup.xhtml", "", -1, false);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		//setupRolePageAccess(p, UserRole.LECTURER);
		
		p = setupPage("EditInstitution", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Institution data;ca#Dades institució;es#Datos institución"), "/private/EditInstitution.xhtml",
				"fa fa-user-plus", 0, true);
				
		//		setupRolePageAccess(p, UserRole.ADMIN);
		
		p = setupPage("VLEsList", "Admin", "institution", LocalizedString.fromStringFormat("en#VLEs list;ca#Llista de VLEs;es#Lista de VLEs"),
				"/private/VLEsList.xhtml", "fa fa-user-plus", 0, true);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		//setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		//setupRolePageAccess(p, UserRole.LECTURER);
		//setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("EditVLE", "Admin", "institution", LocalizedString.fromStringFormat("en#Edit VLE;ca#Editar VLE;es#Editar VLE"),
				"/private/EditVLE.xhtml", "", -1, false);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		
		p = setupPage("ToolsList", "Admin", "ela",
				LocalizedString.fromStringFormat("en#Tools list;ca#Llista d'eines;es#Lista de herramientas"),
				"/private/ToolsList.xhtml", "fa fa-user-plus", 0, true);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		
		p = setupPage("EditTool", "Admin", "ela", LocalizedString.fromStringFormat("en#Edit Tool;ca#Editar eina;es#Editar herramienta"),
				"/private/EditTool.xhtml", "", -1, false);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		
		p = setupPage("AssignmentAssessment", "Admin", "ela",
				LocalizedString.fromStringFormat("en#Assignment assessment;ca#Avaluació activitat;es#Evaluación actividad"),
				"/private/AssignmentAssessment.xhtml", "fa fa-user-plus", 0, true);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		//setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("CoursesReport", "Reports", "ela",
				LocalizedString.fromStringFormat("en#Courses report;ca#Informe assignatures;es#Informe asignaturas"),
				"/private/CoursesReport.xhtml", "fa fa-bar-chart", 0, true);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		//setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		//setupRolePageAccess(p, UserRole.LECTURER);
		//setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("AssignmentsReport", "Reports", "ela",
				LocalizedString.fromStringFormat("en#Assignments report;ca#Informe activitats;es#Informe actividades"),
				"/private/AssignmentsReport.xhtml", "fa fa-bar-chart", 0, true);
		
		setupRolePageAccess(p, UserRole.ADMIN);
		//setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		//setupRolePageAccess(p, UserRole.LECTURER);
		//setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("ExercisesReport", "Reports", "ela",
				LocalizedString.fromStringFormat("en#Exercises report;ca#Informe exercicis;es#Informe ejercicios"), "/private/ExercisesReport.xhtml",
				"fa fa-bar-chart", 0, true);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		setupRolePageAccess(p, UserRole.LECTURER);
		setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("StudentsReport", "Reports", "ela",
				LocalizedString.fromStringFormat("en#Students report;ca#Informe estudiants;es#Informe estudiantes"), "/private/StudentsReport.xhtml",
				"fa fa-bar-chart", 0, true);
		
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		setupRolePageAccess(p, UserRole.LECTURER);
		setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("StudentReport", "Reports", "ela",
				LocalizedString.fromStringFormat("en#Student report;ca#Informe estudiante;es#Informe estudiante"), "/private/StudentReport.xhtml",
				"fa fa-bar-chart", 0, true);
		
		setupRolePageAccess(p, UserRole.STUDENT);
		
		p = setupPage("EtlProcess", "Admin", "ela", LocalizedString.fromStringFormat("en#ETL process;ca#Procés ETL;es#Proceso ETL"),
				"/private/EtlProcess.xhtml", "fa fa-cog", 0, true);
	}	
}
