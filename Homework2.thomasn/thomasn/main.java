/**
 * 
 */
package homework2.thomasn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * @author ThomasNg
 *
 */

public class main {

/**
 * 
 * @param args
 * @throws IOException
 */
	
	/* UNUSED for now. Pattern compiling and Matcher used instead
	public static Predicate<String> hwPattern() {
		return p -> p.matches("(\\A[a-zA-Z-.]{4,}\\z)");
	    //return p -> p.getAge() > 18 && p.getGender().equalsIgnoreCase("F");
	}
	
	public static Predicate<String> hwPatternEndingPeriod() {
		return p -> p.matches("(\\A[a-zA-Z-.]{2,}[\\.]\\z)");
	    //return p -> p.getAge() > 18 && p.getGender().equalsIgnoreCase("F");
	}
	*/
	
    public static void main (String[] args) throws IOException{
    	
    	
    	
		String orgFileName = "original.txt";		
		
		//Pattern pattern = Pattern.compile("(\\A[a-zA-Z-.^[\\p{Punct}]{5,}\\z)");
		
		List<String> words = Files.lines(Paths.get(orgFileName))										
										.map(s -> (s.matches("[a-zA-Z]{3,}+\\W\\s+") ? (s.split("\\W\\s+")) : (s.split("\\s+"))))
										.flatMap(Arrays::stream)
										.collect(Collectors.toList());	

		 
		Collections.sort(words, (a, b) -> b.compareTo(a));		
		System.out.println("\n\tWords as List post sort (compareTo): " + words);
		
		List<String> uniqueWords = new LinkedList<>();
		Set<String> tempHS = new HashSet<>();	//hashset used as temp to avoid dups, then will just copy to list again later.

		//if s has ending punction, removes it (except for ac like "m.d."
		Pattern patLtrsThenPunc = Pattern.compile("(?u)(.{2,}(\\w{3,})(\\p{Punct}{1,2})\\z)", Pattern.CASE_INSENSITIVE);
		
		//if s starts with punc followed by letters, remove punc
		Pattern patPuncThenLtrs = Pattern.compile("(?u)((\\A\\p{Punct}{0,2})(\\w{2,}+))", Pattern.CASE_INSENSITIVE);
		
		//if at least 3 char then punc then term
		Pattern patLtrsThenPuncThenBound = Pattern.compile("(?u)((\\A)(\\w{3,})(\\p{Punct}{1,3})(\\z))", Pattern.CASE_INSENSITIVE);
		
		//if word and not a acryonms: title case it  
		Pattern patNotAcryn = Pattern.compile("(?u)((\\w{3,}))", Pattern.CASE_INSENSITIVE);		
		
		//delete s if all nums, maybe with punc 
		Pattern patAllNum = Pattern.compile("(?u)(((\\A(\\p{Digit}{1,}+))(\\p{Punct}{0,})))", Pattern.CASE_INSENSITIVE);
		
		//delete s if too short
		Pattern patTooShort = Pattern.compile("(?u)(\\A(.{1,3})\\z)", Pattern.CASE_INSENSITIVE);
		
		//TODO Add all regex patterns and replacement regex patterns to a class, then use a hashMap with .forEach to handle the replacements - clean this shit up.
		words.forEach(s -> {
			//s=s.toLowerCase(); //save case modification until the end to preserve case in acronyms
			//temp=temp.toLowerCase(); //set everything to lower case. TODO: make function to do title case instead.
			System.out.println(s);			

		//if s has ending punction, removes it (except for ac like "m.d."
			Matcher matLtrsThenPunc = patLtrsThenPunc.matcher(s);	
			boolean BooLtrsThenPunc = matLtrsThenPunc.matches();
			if (BooLtrsThenPunc==true) s=s.replaceAll("(?u)((\\p{Punct}{1,2})\\z)", "");
			
		//if s starts with punc followed by letters, remove punc
			Matcher matPuncThenLtrs = patPuncThenLtrs.matcher(s);	
			boolean booPuncThenLtrs = matPuncThenLtrs.matches();
			if (booPuncThenLtrs==true) s=s.replaceAll("(?u)(\\A(\\p{Punct}{1,3}))", "");
			
		//if at least 3 char then punc then term
			Matcher matLtrsThenPuncThenBound = patLtrsThenPuncThenBound.matcher(s);	
			boolean booLtrsThenPuncThenBound = matLtrsThenPuncThenBound.matches();
			if (booLtrsThenPuncThenBound==true) s=s.replaceAll("(?u)((\\p{Punct}{1,3}))", "");
			
		//if word and not a acryonms: title case it
			Matcher matNotAcryn = patNotAcryn.matcher(s);			
			boolean booNotAcryn = matNotAcryn.matches();
			if (booNotAcryn==true) s=(s.substring(0,1).toUpperCase()) + (s.substring(1).toLowerCase());
			
		//delete s if all nums, maybe with punc
			Matcher matAllNum = patAllNum.matcher(s);	
			boolean booAllNum = matAllNum.matches();
			if(booAllNum==true) s="";
			
		//delete s if too short
			Matcher matTooShort = patTooShort.matcher(s);	
			boolean booTooShort = matTooShort.matches();
			if(booTooShort==true) s="";
			
			//TODO: title case hyphaneted words
			/*if (s.matches("(?u)((\\b\\w{2,}))")){		//for hypaneted words (like "state-of-the-art") Title case each first letter.
				String camelTemp = s.
				s=(s.substring(0,1).toUpperCase()) + (s.substring(1).toLowerCase());
			}
			*/						
			String temp=s;
			tempHS.add(temp);
		});
		
	//copy all of tempHS (hashset) which should not have dups and be partially/mostly sorted
		uniqueWords.addAll(tempHS); 
		
	//sort Collection UniqueWords to be safe.
		Collections.sort(uniqueWords, (a, b) -> b.compareTo(a));
		
	//TODO: use printwriter in a try/catch block https://www.youtube.com/watch?v=br_TEuE8TbY
		uniqueWords.forEach(System.out::println); 
		
		System.out.println("\n\n\t\tTOTAL UNIQUE WORDS: " + uniqueWords.size());
	    String uniqueFile = "uniqueWords.txt";
	    //Path uniqueFilePath = Paths.get(uniqueFile); //unused
	    //String uniqueFilePathString = uniqueFilePath.toString();
	    
	//TODO try/catch, check for file, etc.
	    Files.write(Paths.get(uniqueFile), uniqueWords);
	    
	    
	    /* TODO: use buffered writer
	    BufferedWriter uniqueWriter = new BufferedWriter(new FileWriter(uniqueFile));
	    try {
			uniqueWords.forEach(s ->uniqueWriter.write(s));
			uniqueWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	    */
		
		
    }
	

    

        
}