# -*- coding: utf-8 -*-
"""
Created on Thu Aug 24 17:16:20 2017

@author: you
"""
import random
import numpy as np
import sys

filename = sys.argv[1:][0]

class data_set(): 
    data=[]
    label=[]
    train=[]
    test=[] 
    ratio =0.8
    def __init__(self, file_name):
         
        self.read_data(file_name)
        
    def read_data(self, filename):
        f = open ( filename , 'r+')
        l=[]
        l2=[]
        normal_length=0 #lenth of normal vector
        for line in f:
            line = line.replace(',','.')
            s  = line.split(' ')
            s.remove('\n')
#            print(s)
            l.append(list(map(float,s[0:-1])))
            if list(map(float,s[-1:]))[0]==0:
                l2.append([1,0])
                normal_length+=1
            else:
                l2.append([0,1])
#            elif list(map(float,s[-1:]))[0]==1:
#                l2.append([0,1])
#            elif list(map(float,s[-1:]))[0]==2:
#                l2.append([0,1])
                
            #	l = [ map(float,line.split(' ')) for line in f ]
#        for line in l:
#            print (line)

        self.data = np.asarray(l,float)
        self.label = np.asarray(l2,float)
        
        
        
        index_10 = [ i for i in range (normal_length)]#index of the normal bidder

        index_01 = [ i+normal_length for i in range (len(self.label)-normal_length)]#index of the fraud bidder
        random.shuffle(index_10)
        random.shuffle(index_01)
        self.train = index_10[0:(int)(len(index_10)*self.ratio)] + index_01[0:(int)(len(index_01)*self.ratio)]
        self.test = index_10[-(int)(len(index_10)*(1-self.ratio)):] + index_01[-(int)(len(index_01)*(1-self.ratio)):]
        
        
    def next_train_batch(self, num):
        random.shuffle(self.train)
        ls = self.train[0:num]
        return [[self.data[i] for i in ls], [self.label[i] for i in ls]]
        
    def get_test_batch(self):
        return [[self.data[i] for i in self.test], [self.label[i] for i in self.test]]
        






from tensorflow.examples.tutorials.mnist import input_data

"""
mnist = input_data.read_data_sets('MNIST_data', one_hot=True)
"""
#mnist = data_set("./Documents/test.txt")


#print (filename)
mnist = data_set(filename)

#print (filename)

#exit()


import tensorflow as tf
sess = tf.InteractiveSession()
#print (mnist.data)
#exit()

input_size = len(mnist.data[0])

x = tf.placeholder(tf.float32, shape=[None, input_size],name="x")
y_ = tf.placeholder(tf.float32, shape=[None, 2],name="y_")

W = tf.Variable(tf.zeros([input_size,2]),name="W")
b = tf.Variable(tf.zeros([2]),name="b")

sess.run(tf.global_variables_initializer())

y = tf.matmul(x,W) + b

cross_entropy = tf.reduce_mean(
    tf.nn.softmax_cross_entropy_with_logits(labels=y_, logits=y))

train_step = tf.train.GradientDescentOptimizer(0.5).minimize(cross_entropy)

for _ in range(10000):
  batch = mnist.next_train_batch( 100)
  train_step.run(feed_dict={x: batch[0], y_: batch[1]})
  
correct_prediction = tf.equal(tf.argmax(y,1,name="prediction"), tf.argmax(y_,1))
accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))

test =mnist.get_test_batch()
pred = tf.get_default_graph().get_operation_by_name("prediction")
#print(sess.run(y, feed_dict={x: test[0]}))
#print(sess.run(y_, feed_dict={y_: test[1]}))
print("accuracy = ", accuracy.eval(feed_dict={x: test[0], y_: test[1]})),
#
#print(ok)
#exit()

saver = tf.train.Saver()
save_path = saver.save(sess, "./tmp/model.ckpt")


"""

# Add ops to save and restore all the variables.
saver = tf.train.Saver()

# Later, launch the model, initialize the variables, do some work, and save the
# variables to disk.
with tf.Session() as sess:
  sess.run(init_op)
  # Do some work with the model.
  inc_v1.op.run()
  dec_v2.op.run()
  # Save the variables to disk.
  save_path = saver.save(sess, "/tmp/model.ckpt")
  print("Model saved in file: %s" % save_path)
  
"""
"""

# Add ops to save and restore all the variables.
saver = tf.train.Saver()

# Later, launch the model, use the saver to restore variables from disk, and
# do some work with the model.
with tf.Session() as sess:
  # Restore variables from disk.
  saver.restore(sess, "/tmp/model.ckpt")
  print("Model restored.")
  # Check the values of the variables
  print("v1 : %s" % v1.eval())
  print("v2 : %s" % v2.eval())
"""







