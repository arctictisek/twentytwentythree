# utils.py

def read_file_to_tuple(filename):
    with open(filename, 'r') as file:
        return tuple(line.strip() for line in file)


def read_file_to_list(filename):
    with open(filename, 'r') as file:
        return list(line.strip() for line in file)
