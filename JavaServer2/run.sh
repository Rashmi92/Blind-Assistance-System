#!/bin/bash

'''
cd /data5/patange.rashmi/Latest_project/neuraltalk2-master

cd /data5/patange.rashmi/
ls
'''
#sshpass -p "patange123" scp -r /home/rashmi/CVProject/Notes/Sample/ patange.rashmi@atom.cvit.iiit.ac.in:/data5/patange.rashmi/Latest_project/neuraltalk2-master/
sshpass -p 'patange123' ssh -o StrictHostKeyChecking=no -X patange.rashmi@atom.cvit.iiit.ac.in << EOSSH
echo "patange123" | ssh node10
cd /data5/patange.rashmi/Latest_project/neuraltalk2-master/
ls -lt
export CPATH=/users/patange.rashmi/cuda/include:$CPATH
export LIBRARY_PATH=/users/patange.rashmi/cuda/lib64:$LIBRARY_PATH
export LD_LIBRARY_PATH=/users/patange.rashmi/cuda/lib64:$LD_LIBRARY_PATH
echo "here"
th eval.lua -model model_id.t7 -image_folder Sample/ -num_images 2

EOSSH
sshpass -p "patange123" scp patange.rashmi@atom.cvit.iiit.ac.in:/data5/patange.rashmi/Latest_project/neuraltalk2-master/vis/vis.json /home/rashmi/CVProject/Notes/Result/ 
