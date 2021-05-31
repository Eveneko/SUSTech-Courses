import os

# get the folder name
folder_name = os.path.dirname(__file__)
# folder_name = input("Input the folder name：")


# get all files name
file_names = os.listdir(folder_name)

print(file_names)
for i, name in enumerate(file_names):
    old_file_name = folder_name + "/" + name

	# demo1: for http://www.pdfdo.com/pdf-to-image.aspx
    name1 = (name.split('_')[-1]).split('.')[0]
    new_file_name = folder_name + "/" + 'p' + name1 + '.png'
    
    # for recover demo1
    # name1 = name.lower().replace('-', '_')
    # new_file_name = folder_name + "/" + 'name[1:-4]
    
    os.rename(old_file_name, old_file_name.replace('txt', 'csv'))
