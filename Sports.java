package sports;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;



 
/**
 * Facade class for the research evaluation system
 *
 */
public class Sports {
	
	Map <String, Set <String>> ListActivities = new  TreeMap<>();
	Map <String, Set<String>> categories = new TreeMap <>();
    Map <String, Product> products = new TreeMap<>();
    
    Map <Product, List<Rating>> ratings = new HashMap<>();

    //R1
    /**
     * Define the activities types treated in the portal.
     * The method can be invoked multiple times to add different activities.
     * 
     * @param actvities names of the activities
     * @throws SportsException thrown if no activity is provided
     */
    public void defineActivities (String... activities) throws SportsException {
    	
    	if (activities.length ==0)
			throw new SportsException("activity already present!\n");
    	for ( String act : activities)
    	{
    		ListActivities.put(act, new TreeSet<>());
    	}
    }

    
    /**
     * Retrieves the names of the defined activities.
     * 
     * @return activities names sorted alphabetically
     */
    public List<String> getActivities() {
    	
        return ListActivities.keySet().stream().collect(Collectors.toList());
    }


    /**
     * Add a new category of sport products and the linked activities
     * 
     * @param name name of the new category
     * @param activities reference activities for the category
     * @throws SportsException thrown if any of the specified activity does not exist
     */
    public void addCategory(String name, String... linkedActivities) throws SportsException {
    	
    	for (String act : linkedActivities)
    	{
    		if (!ListActivities.containsKey(act))
    			throw new SportsException(" attività non presente \n");
    		
    		
    		categories.put(name, new TreeSet<>());
    		categories.get(name).add(act);
    		ListActivities.get(act).add(name);
    	}
    	
    }

    /**
     * Retrieves number of categories.
     * 
     * @return categories count
     */
    public int countCategories() {
        return categories.size();
    }

    /**
     * Retrieves all the categories linked to a given activity.
     * 
     * @param activity the activity of interest
     * @return list of categories (sorted alphabetically)
     */
    public List<String> getCategoriesForActivity(String activity) {
    	
    	
    	List<String> list = new ArrayList<>();
    	if (!ListActivities.containsKey(activity) || ListActivities.get(activity).isEmpty())
    		return list;
    	else {
        return ListActivities.get(activity).stream().collect(Collectors.toList());}
    }

    //R2
    /**
     * Add a research group and the relative disciplines.
     * 
     * @param name name of the research group
     * @param disciplines list of disciplines
     * @throws SportsException thrown in case of duplicate name
     */
    public void addProduct(String name, String activityName, String categoryName) throws SportsException {
    	
    	Product p = new Product(name, categoryName, activityName);
    	
    	if (products.containsKey(name))
    		throw new SportsException("prodotto già presente \n");
    	products.put(name,p);
    		
    }

    /**
     * Retrieves the list of products for a given category.
     * The list is sorted alphabetically.
     * 
     * @param categoryName name of the category
     * @return list of products
     * 
     *     Map <String, Product> products = new TreeMap<>();
     */
    public List<String> getProductsForCategory(String categoryName){
    	
        return  products.entrySet().stream()
    			.filter( p-> p.getValue().getCategory().equals(categoryName))
    			.map(p->p.getKey())
    			.collect(Collectors.toList());	
        
 /*oOR return  products.values().stream()
    *			 .filter(p -> p.getCategory().equals(categoryName) )
				.collect(Collectors.mapping(Product::getName, Collectors.toList()));

         */
        
    }

    /**
     * Retrieves the list of products for a given activity.
     * The list is sorted alphabetically.
     * 
     * @param activityName name of the activity
     * @return list of products
     */
    public List<String> getProductsForActivity(String activityName){
    	
       return products.entrySet().stream()
    		   .filter( p-> p.getValue().getActivity().equals(activityName))
    			.map( p->p.getKey())
    			.collect(Collectors.toList());
   /* 
    * return products.values().stream().filter( p -> p.getActivity().equals(activityName) )
				.collect(Collectors.mapping(Product::getName, Collectors.toList()));
    											mapper			downstream collector
    */
        
    }
    /**
     * Retrieves the list of products for a given activity and a set of categories
     * The list is sorted alphabetically.
     * 
     * @param activityName name of the activity
     * @param categoryNames names of the categories
     * @return list of products
     */
    public List<String> getProducts(String activityName, String... categoryNames){
    	
    	List<String> tmp = Arrays.asList(categoryNames);
    	
    	return products.values().stream()
    			.filter(p-> p.getActivity().equals(activityName) && tmp.contains(p.getCategory()))
    			.collect(Collectors.mapping(Product::getName, Collectors.toList()));
    	 }

    
    /* OR WITHOUT STREAM:
     * List<String> list = new ArrayList<>();

    	List<String> listA = getProductsForActivity( activityName);
    
    	if ( !ListActivities.containsKey(activityName) )
    		return new ArrayList<>();
    	
    	for (String cat : categoryNames)
    	{
    		List<String> listC = getProductsForCategory(cat);
   
    		for ( String name : listC) {
    			for ( String name2 : listA) {
    				if (name2.equals(name))
    					list.add(name);
    			}
    		}
    	}	Collections.sort(list);
    		
        return list; 
     */
    //    //R3
    /**
     * Add a new product rating
     * 
     * @param productName name of the product
     * @param userName name of the user submitting the rating
     * @param numStars score of the rating in stars
     * @param comment comment for the rating
     * @throws SportsException thrown numStars is not correct
     */
    public void addRating(String productName, String userName, int numStars, String comment) throws SportsException {
   
    	Rating r = new Rating(userName, numStars, comment);
    	if( !r.formatStar())
    		throw new SportsException("wrong star format");
    	
    	Product p = products.get(productName);
    	// if key already present, instantiate the new list of ratings
    	if( !ratings.containsKey(p))
    		ratings.put(p, new ArrayList<>()); 
    	//then add the new rating
    	ratings.get(p).add(r);
    	
    }

    /**
     * Retrieves the ratings for the given product.
     * The ratings are sorted by descending number of stars.
     * 
     * @param productName name of the product
     * @return list of ratings sorted by stars
     */
    public List<String> getRatingsForProduct(String productName) {
    	
    	Product p = products.get(productName);
    	
    	if (  !ratings.containsKey(p) || ratings.get(p).isEmpty() )
    		{System.out.println(" not contained");
    		return  new ArrayList<String>();
    		}
        		
        return ratings.get(p).stream()
        		.sorted(Comparator.comparing(Rating::getStar).reversed())
        		.map (t -> t.toString())
        		.collect(Collectors.toList());
    }
   

    //R4
    /**
     * Returns the average number of stars of the rating for the given product.
     * 
     * 
     * @param productName name of the product
     * @return average rating
     */
    public double getStarsOfProduct (String productName) {
    	
        return ratings.get(products.get(productName)).stream()
        		.mapToDouble(p-> p.getStar())
        		.average()
        		.getAsDouble();
    }

    /**
     * Computes the overall average stars of all ratings
     *  
     * @return average stars
     */
    public double averageStars() {
        return  ratings.values().stream() //stream of List<string>
        		.flatMap(Collection::stream) //extract a stream of rating
        		.mapToDouble(p-> p.getStar() ) //within the stream, select only one attribute
        		.average()
        		.getAsDouble();

    }

    //R5 Statistiche
    /**
     * For each activity return the average stars of the entered ratings.
     * 
     * Activity names are sorted alphabetically.
     * 
     * @return the map associating activity name to average stars
     */
    public SortedMap<String, Double> starsPerActivity() {
    	
    	
        return ratings.entrySet().stream()
        		.collect(Collectors.groupingBy(r -> r.getKey().getActivity(),
        				TreeMap::new, 
        				Collectors.averagingDouble( r -> r.getValue().stream().mapToDouble(l->l.getStar()).average().getAsDouble() ) ));}
  
  /*	SortedMap<String, List <Integer>> list = new TreeMap<>();
    	for ( Product product : ratings.keySet()) {
    		
    		if ( !list.containsKey(product.getActivity()) && !ratings.get(product).isEmpty())
    			{list.put(product.getActivity(), new ArrayList<>());}
    		
    		for ( Rating r : ratings.get(product))
    			{list.get(product.getActivity()).add(r.getStar());}
    			
    	}	SortedMap<String, Double> list1 = new TreeMap<>();
    	
    	for (String s : list.keySet())
    	{	double avg = list.get(s).stream().mapToDouble(p->p).average().getAsDouble();
    		list1.put(s, avg );
    	}
    
        return  list1;  
   */

    /**
     * For each average star rating returns a list of
     * the products that have such score.
     * 
     * Ratings are sorted in descending order.
     * 
     * @return the map linking the average stars to the list of products
     */
    
   //     Map <Product, List<Rating>> ratings = new HashMap<>();
    
    public SortedMap<Double, List<String>> getProductsPerStars () {
    	
     SortedMap<Double, List<String>> list = new TreeMap<>();
   //per ogni prodotto, calcolo la media di stelle	 
    	 for ( Product p : ratings.keySet()) 
    	 {
    		 double avg = getStarsOfProduct(p.getName());
    		 	if ( !list.containsKey(avg)) 			//se la key non l'ho già aggiunta
    		 		list.put(avg, new ArrayList<>()); 	//creo nuova entry e nuova lista vuota per i prodotti
    		 
    		 list.get(avg).add(p.getName());			//aggiungo il nome del prodotto
    	 }
   // creo una map uguale ma con le key in ordine decrescente
   	 SortedMap<Double, List<String>> list2 = new TreeMap<>(Collections.reverseOrder());
  //copio la lista precedente, l'ordinamento è automatico
   	 list2.putAll(list); 
   	 
   // ordino la lista di prodotti
   	 for ( List<String> product : list2.values())
   	 {
   		 product.sort(Comparator.naturalOrder());
   	 }
 	
        return list2;
  
    }

}