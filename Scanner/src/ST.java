import java.util.*;

// 1a for next lab

public class ST {
    private Integer nrElements;
    private Integer size;
    private List<Object>[] elements;

    public ST(Integer size) {
        this.nrElements = 0;
        this.size = size;
        this.elements = new ArrayList[this.size];
        for(var i = 0; i < this.size; i ++){
            this.elements[i] = new ArrayList<>();
        }
    }

    public List<Object>[] getElements() {
        return elements;
    }

    private int hashFunction(Object element) {
        Integer f = -1;
        if(element instanceof String) {
             f = ((String)element).chars().reduce(Integer::sum).orElseThrow();
        }else if(element instanceof Integer){
            f = (((Integer)element).toString()).chars().reduce(Integer::sum).orElseThrow();
        }
        return f % this.size;
    }

    private Map<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>> resize() {
        var positionsTranformed = new HashMap<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>>();
        this.size = this.size * 2;
        var newElements = new ArrayList[this.size];
        for(var i = 0; i < this.size; i ++){
            newElements[i] = new ArrayList<>();
        }
        this.nrElements = 0;
        for(var oldBucket = 0; oldBucket < this.elements.length; oldBucket ++){
            var list = this.elements[oldBucket];
            for(var pos = 0; pos < list.size(); pos++){
                var element = list.get(pos);
                var bucket = this.hashFunction(element);
                newElements[bucket].add(element);
                this.nrElements +=1 ;
                positionsTranformed.put(new AbstractMap.SimpleEntry<>(oldBucket,pos),
                        new AbstractMap.SimpleEntry<>(bucket,newElements[bucket].size()-1));
            }
        }
        this.elements = newElements;
        return positionsTranformed;
    }

    private AbstractMap.SimpleEntry<Integer, Integer>  getIdentifierPosition(Object element) {
        var position = new AbstractMap.SimpleEntry<>(-1,-1);
        var bucket = this.hashFunction(element);
        for(var i = 0; i < this.elements[bucket].size(); i ++){
            var el = this.elements[bucket].get(i);
            if(el.equals(element)){
                position = new AbstractMap.SimpleEntry<>(bucket, i);
            }
        }
        return position;
    }

    public AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Integer, Integer>, Map<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>>> addElement(Object element) {
        AbstractMap.SimpleEntry<Integer, Integer> existingPosition = getIdentifierPosition(element);
        Map<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>> transformed = null;
        if(existingPosition.equals(new AbstractMap.SimpleEntry<>(-1,-1))){
            if(this.nrElements > this.size * 2 ){
                transformed = this.resize();
            }
            var bucket = this.hashFunction(element);
            var posInBucket = this.elements[bucket].size();
            this.elements[bucket].add(element);
            existingPosition = new AbstractMap.SimpleEntry<>(bucket, posInBucket);
            this.nrElements +=1 ;
        }
        return new AbstractMap.SimpleEntry<>(existingPosition, transformed);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        for ( var bucket = 0; bucket < elements.length ; bucket ++) {
            builder.append(bucket).append(" => ");
            for(var pos = 0; pos < elements[bucket].size(); pos ++) {
                builder.append(pos).append(": ").append(elements[bucket].get(pos)).append(" | ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
