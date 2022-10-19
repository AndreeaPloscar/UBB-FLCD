
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

    private int hashFunction(Object identifier) {
        Integer f = -1;
        if(identifier instanceof String) {
             f = ((String)identifier).chars().reduce(Integer::sum).orElseThrow();
        }else if(identifier instanceof Integer){
            f = (((Integer)identifier).toString()).chars().reduce(Integer::sum).orElseThrow();
        }
        return f % this.size;
    }

    private void resize() {
        this.size = this.size * 2;
        var newElements = new ArrayList[this.size];
        for(var i = 0; i < this.size; i ++){
            newElements[i] = new ArrayList<>();
        }
        this.nrElements = 0;
        for(var list: this.elements){
            for(var element: list){
                var bucket = this.hashFunction(element);
                newElements[bucket].add(element);
                this.nrElements +=1 ;
            }
        }
        this.elements = newElements;
    }

    private AbstractMap.SimpleEntry<Integer, Integer> getIdentifierPosition(Object identifier) {
        var position = new AbstractMap.SimpleEntry<>(-1,-1);
        var bucket = this.hashFunction(identifier);
        for(var i = 0; i < this.elements[bucket].size(); i ++){
            var element = this.elements[bucket].get(i);
            if(element.equals(identifier)){
                position = new AbstractMap.SimpleEntry<>(bucket, i);
            }
        }
        return position;
    }

    public AbstractMap.SimpleEntry<Integer, Integer> addIdentifier(Object identifier) {
        AbstractMap.SimpleEntry<Integer, Integer> existingPosition = getIdentifierPosition(identifier);
        if(existingPosition.equals(new AbstractMap.SimpleEntry<>(-1,-1))){
            if(this.nrElements > this.size * 2 ){
                this.resize();
            }
            var bucket = this.hashFunction(identifier);
            var posInBucket = this.elements[bucket].size();
            this.elements[bucket].add(identifier);
            existingPosition = new AbstractMap.SimpleEntry<>(bucket, posInBucket);
            this.nrElements +=1 ;
        }
        return existingPosition;
    }
}
