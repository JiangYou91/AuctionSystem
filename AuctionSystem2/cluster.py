
import numpy as np



def readData(filename):
    f = open ( filename , 'r+')
    l=[]
    for line in f:
        line = line.replace(',','.')
        s  = line.split(' ') 
        l.append(list(map(float,s[:-1])))
#	l = [ map(float,line.split(' ')) for line in f ]

    f.close()
    return np.asarray(l,float)


def writeData(filename,mat):
    f = open ( filename , 'w+') 
    for data in mat:  
        for d in data:
            s = ""
            for v in d:
                s+=str(v)+" "
            s+="\n"
            f.write(s)
    f.close()
    

def normalise(mat):
    m=np.copy(mat[0])
    for l in range(mat.shape[0]):
        for c in range(mat.shape[1]):
            if mat[l,c]>m[c]:
                m[c] = mat[l,c]
    return mat/m

class Cluster:
    def __init__(self, data,num):
        self.data=[data]
        self.average = data
        self.num=[num]
    def __str__( self  ):
        return "c"+str(self.num)+str(self.data)
    def __repr__(self):
        return "c"+str(self.num)+str(self.data)
    def merge(self, cluster): 
        self.data = np.vstack((self.data,cluster.data))
        self.average = np.sum(self.data, axis=0)/len(self.data)
        self.num=self.num+cluster.num
    

def createCluster(mat):
    c = []
    for i in range(mat.shape[0]):
        c.append(  Cluster(mat[i],i))
    return c

def sortCluster(cluster):
    cluster= sorted(cluster, key = lambda x : len(x.num),reverse = True)
    return cluster

def labelCluster(cluster,mat):
    labeled_data = []
    i=0
    for c in cluster:
        d = mat[c.num]
#        print (d)
        label = np.ones([len(d),1])*i
#        print (label)
#        print (np.hstack((d, label)))
        labeled_data.append( np.hstack((d, label)))
        i+=1
    return labeled_data

def similarity(c1,c2):
#   print c1, c2,"inner = ", np.inner(c1.average, c1.average), np.inner( c2.average, c2.average), np.inner(c1.average, c2.average)
   if np.inner(c1.average, c1.average) ==0 or np.inner( c2.average, c2.average)==0:
        return 1
   else:
       return np.inner(c1.average, c2.average)/(np.linalg.norm(c1.average,2) *np.linalg.norm(c2.average,2))

"""clusters init to all points """

def clustering(clusters,minSimilarity):
    hasSimilerCluster = True;
    while hasSimilerCluster :
       hasSimilerCluster = False;
       max_sim=0 
       print(len(clusters))
       for c1 in clusters: 
           for c2 in clusters: 
#               print "trait" , c1, c2,"sim = ", similarity(c1,c2)
               if c1!=c2:
                   sim = similarity(c1,c2)
                   if sim> minSimilarity and sim> max_sim:
                       max_sim=sim
                       max_c1=c1
                       max_c2=c2
                       hasSimilerCluster = True
                       max_c1.merge(max_c2)
                       clusters.remove(max_c2)
                       """break;""" 
#       if hasSimilerCluster:
           
    return clusters



import sys
if (len(sys.argv)!=3):
    print("python cluster.py <input file name> <output file name>")
    exit(0)
#print(sys.argv)
input_file = sys.argv[1]
output_file = sys.argv[2]

#   
#"""test:"""
#a = np.array([[1,2,3],[1,2,2],[11,10,2],[10,12,1],[10,10,3],[5,5,5]]) 
#minSimilarity = 0.975
#
#
#clusters =  createCluster(a)
##similarity(clusters[0],clusters[1])
#clusters = clustering(clusters,minSimilarity)
#clusters = sortCluster(clusters)
#labeled_clusters = labelCluster(clusters)
#print (labeled_clusters)
#
#writeData('./Documents/AuctionSystem2/test_write.txt',labeled_clusters)

#
#np.ones([3,1])*0


"""tests:"""

#mat = readData('./Documents/test.txt')
mat = readData(input_file)
#print (mat)
normal_mat = normalise(mat)

minSimilarity = 0.98

clusters =  createCluster(normal_mat)

clusters = clustering(clusters,minSimilarity)
clusters = sortCluster(clusters)
labeled_clusters = labelCluster(clusters,mat)
#writeData('./Documents/AuctionSystem2/test_write.txt',labeled_clusters)
writeData(output_file,labeled_clusters)
#print (clusters)










